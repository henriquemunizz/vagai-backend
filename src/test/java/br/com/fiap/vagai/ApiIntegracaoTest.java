package br.com.fiap.vagai;

import br.com.fiap.vagai.service.*;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import javax.sql.DataSource;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
    "spring.datasource.url=jdbc:h2:mem:vagai;MODE=Oracle;DB_CLOSE_DELAY=-1",
    "spring.datasource.username=sa", "spring.datasource.password="})
class ApiIntegracaoTest {
    @org.junit.jupiter.api.io.TempDir Path temporario;
    @Value("${local.server.port}") int porta;
    @Autowired DataSource origem;
    @Autowired SincronizacaoOsm sincronizacao;
    final ObjectMapper json = new ObjectMapper();

    class Cliente {
        final HttpClient http = HttpClient.newBuilder().cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL)).build();

        JsonNode enviar(String metodo, String rota, Object corpo, int esperado) throws Exception {
            var pedido = HttpRequest.newBuilder(URI.create("http://localhost:" + porta + rota))
                    .header("Content-Type", "application/json").method(metodo, corpo == null ?
                            HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(json.writeValueAsString(corpo)));
            var resposta = http.send(pedido.build(), HttpResponse.BodyHandlers.ofString());
            assertEquals(esperado, resposta.statusCode(), metodo + " " + rota + " " + resposta.body());
            if (esperado == 201) assertTrue(resposta.headers().firstValue("Location").isPresent());
            if (esperado == 204) { assertEquals("", resposta.body()); return null; }
            JsonNode dados = json.readTree(resposta.body());
            if (esperado >= 400) assertEquals(esperado, dados.get("status").asInt());
            return dados;
        }

        JsonNode get(String rota, int status) throws Exception { return enviar("GET", rota, null, status); }
        JsonNode post(String rota, Object corpo, int status) throws Exception { return enviar("POST", rota, corpo, status); }
        void put(String rota, Object corpo, int status) throws Exception { enviar("PUT", rota, corpo, status); }
        void delete(String rota, int status) throws Exception { enviar("DELETE", rota, null, status); }
    }

    @Test
    void corsParaReact() throws Exception {
        var http = HttpClient.newHttpClient();
        for (String origemTeste : List.of("http://localhost:5173", "https://outra.example")) {
            var pedido = HttpRequest.newBuilder(URI.create("http://localhost:" + porta + "/auth/login"))
                    .header("Origin", origemTeste).header("Access-Control-Request-Method", "POST")
                    .header("Access-Control-Request-Headers", "content-type")
                    .method("OPTIONS", HttpRequest.BodyPublishers.noBody()).build();
            var resposta = http.send(pedido, HttpResponse.BodyHandlers.ofString());
            if (origemTeste.equals("http://localhost:5173")) {
                assertEquals(200, resposta.statusCode());
                assertEquals(origemTeste, resposta.headers().firstValue("Access-Control-Allow-Origin").orElseThrow());
                assertEquals("true", resposta.headers().firstValue("Access-Control-Allow-Credentials").orElseThrow());
            } else {
                assertEquals(403, resposta.statusCode());
                assertEquals(403, json.readTree(resposta.body()).get("status").asInt());
                assertTrue(resposta.headers().firstValue("Access-Control-Allow-Origin").isEmpty());
            }
        }
    }

    @Test
    void jornadaDeDoisMotoristas() throws Exception {
        var banco = new JdbcTemplate(origem);
        new ResourceDatabasePopulator(new FileSystemResource("sql/01_estrutura.sql"),
                new FileSystemResource("sql/02_dados_teste.sql")).execute(origem);
        for (String tabela : List.of("usuario", "estacionamento", "regra_estacionamento", "reporte", "avaliacao", "usuario_favorito"))
            assertEquals(tabela.equals("usuario_favorito") ? 11 : 12,
                    banco.queryForObject("select count(*) from tb_" + tabela, Integer.class));
        String consultas = Files.readString(Path.of("sql/03_consultas.sql")).replaceAll("(?m)^--.*$", "");
        int total = 0;
        for (String sql : consultas.split(";")) if (!sql.isBlank()) { banco.queryForList(sql); total++; }
        assertEquals(10, total);

        Cliente ana = new Cliente(), bia = new Cliente(), publico = new Cliente();
        Map<String,String> conta = Map.of("nome", "motorista-a", "senha", UUID.randomUUID().toString());
        var outra = Map.of("nome", "motorista-b", "senha", UUID.randomUUID().toString());
        String usuarios = "/usuarios/";
        long idAna = ana.post("/auth/cadastro", conta, 201).get("id").asLong();
        String perfil = usuarios + idAna;
        bia.post("/usuarios", outra, 201);
        ana.post("/auth/cadastro", conta, 409);
        ana.post("/auth/cadastro", Map.of("nome", "x", "senha", "x"), 400);
        publico.get("/auth/me", 401);
        ana.post("/auth/login", Map.of("nome", "motorista-a", "senha", UUID.randomUUID().toString()), 401);
        ana.post("/auth/login", conta, 200);
        bia.post("/auth/login", outra, 200);
        assertFalse(ana.get("/auth/me", 200).toString().contains("senha"));
        assertEquals(1, ana.get("/usuarios", 200).size());
        ana.get(perfil, 200);
        bia.get(perfil, 403);
        bia.put(perfil, conta, 403);
        bia.delete(perfil, 403);

        var local = new HashMap<String,Object>(Map.of("nome", "Local colaborativo", "tipo", "ROTATIVO",
                "latitude", 0, "longitude", 0, "ativo", 1));
        String locais = "/estacionamentos";
        publico.post(locais, local, 401);
        long id = ana.post(locais, local, 201).get("id").asLong();
        String lugar = locais + "/" + id;
        assertTrue(publico.get(lugar, 200).get("verificacao").isNull());
        publico.get(locais, 200);
        bia.put(lugar, local, 403);
        bia.delete(lugar, 403);
        local.put("longitude", 0.001);
        ana.put(lugar, local, 200);
        var nota = Map.of("nota", 5, "comentario", "Boa localização");
        var relato = Map.of("tipo", "SEM_VAGA");
        String av = "/avaliacoes/" + ana.post(lugar + "/avaliacoes", nota, 201).get("id").asLong();
        String re = "/reportes/" + ana.post(lugar + "/reportes", relato, 201).get("id").asLong();
        var avaliacao = Map.of("nota", 4, "idEstacionamento", id);
        var reporte = Map.of("tipo", "VAGA_ENCONTRADA", "idEstacionamento", id);
        String av2 = "/avaliacoes/" + bia.post("/avaliacoes", avaliacao, 201).get("id").asLong();
        String re2 = "/reportes/" + bia.post("/reportes", reporte, 201).get("id").asLong();
        bia.delete(av2, 204);
        bia.delete(re2, 204);
        var regra = Map.of("descricao", "Até duas horas", "idEstacionamento", id, "ativo", 1);
        String rg = "/regras/" + ana.post("/regras", regra, 201).get("id").asLong();
        bia.post("/regras", regra, 403);
        var favorito = Map.of("idEstacionamento", id);
        String fav = "/favoritos/" + ana.post("/favoritos", favorito, 201).get("id").asLong();
        ana.post("/favoritos", favorito, 409);
        assertEquals(1, ana.get("/usuarios/me/favoritos", 200).size());
        assertEquals(0, bia.get("/favoritos", 200).size());
        bia.get(fav, 403);
        for (String rota : List.of("/avaliacoes", "/reportes", "/regras")) {
            publico.get(rota, 200);
            assertEquals(1, publico.get(lugar + rota, 200).size());
        }
        for (String rota : List.of(av, re, rg)) publico.get(rota, 200);
        ana.get(fav, 200);
        for (var item : Map.of(av, avaliacao, re, reporte, rg, regra, fav, favorito).entrySet()) {
            bia.put(item.getKey(), item.getValue(), 403);
            bia.delete(item.getKey(), 403);
            ana.put(item.getKey(), item.getValue(), 200);
        }
        ana.delete(lugar, 409);
        ana.post(lugar + "/avaliacoes", Map.of("nota", 6), 400);
        ana.post("/estacionamentos/999999/reportes", relato, 404);
        String busca = locais + "/proximos?latitude=0&longitude=0&raioKm=1&tipo=ROTATIVO";
        var proximo = publico.get(busca, 200).get(0);
        assertEquals(4, proximo.get("mediaAvaliacoes").asInt());
        assertEquals(0.1112, proximo.get("distanciaKm").asDouble(), 0.0001);
        assertEquals(1, proximo.get("regras").size());
        assertTrue(proximo.get("ultimoReporteRecente").get("atual").asBoolean());
        var perto = new HashMap<>(local);
        perto.put("longitude", 0);
        String outro = locais + "/" + ana.post(locais, perto, 201).get("id").asLong();
        var ordenados = publico.get(busca, 200);
        assertEquals(2, ordenados.size());
        assertEquals(0.0, ordenados.get(0).get("distanciaKm").asDouble());
        assertEquals(id, ordenados.get(1).get("id").asLong());
        assertEquals(1, publico.get(busca.replace("raioKm=1", "raioKm=0.05"), 200).size());
        ana.delete(outro, 204);
        banco.update("UPDATE tb_reporte SET dt_reporte = TIMESTAMP '2020-01-01 00:00:00' WHERE id_estacionamento = ?", id);
        assertTrue(publico.get(busca, 200).get(0).get("ultimoReporteRecente").isNull());
        assertFalse(publico.get(re, 200).get("atual").asBoolean());
        publico.get(busca.replace("latitude=0", "latitude=91"), 400);
        local.put("ativo", 0);
        ana.put(lugar, local, 200);
        ana.post(lugar + "/reportes", relato, 409);
        assertEquals(0, publico.get(busca, 200).size());
        local.put("ativo", 1);
        local.put("tipo", "PRIVADO");
        ana.put(lugar, local, 200);
        ana.post(lugar + "/reportes", relato, 409);
        publico.get("/inexistente", 404);
        publico.enviar("PATCH", lugar, local, 405);

        verificarImportacao(ana, banco);
        for (String rota : List.of(fav, rg, re, av)) ana.delete(rota, 204);
        ana.delete(lugar, 204);
        publico.get(lugar, 404);
        ana.put(perfil, conta, 200);
        ana.post("/auth/logout", null, 204);
        ana.get("/auth/me", 401);
        ana.post("/auth/login", conta, 200);
        ana.delete(perfil, 204);
        ana.get("/auth/me", 401);
        banco.execute("ALTER TABLE tb_reporte RENAME TO tb_reporte_indisponivel");
        try {
            assertEquals("Falha interna ao atender a requisição.", publico.get("/reportes", 500).get("mensagem").asString());
        } finally {
            banco.execute("ALTER TABLE tb_reporte_indisponivel RENAME TO tb_reporte");
        }
    }

    private void verificarImportacao(Cliente cliente, JdbcTemplate banco) throws Exception {
        var corpo = new AtomicReference<>("""
                {"version":0.6,"elements":[{"type":"way","id":42,"center":{"lat":0,"lon":0},
                "tags":{"amenity":"parking","operator:type":"private","access":"yes","fee":"yes","name":"Teste OSM"}},
                {"type":"node","id":42,"lat":0,"lon":0,
                "tags":{"amenity":"parking","operator:type":"private","access":"yes"}},
                {"type":"relation","id":42,"center":{"lat":0,"lon":0},"tags":{"amenity":"parking"}}]}
                """);
        HttpServer servidor = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        servidor.createContext("/", troca -> {
            byte[] dados = corpo.get().getBytes(StandardCharsets.UTF_8);
            troca.sendResponseHeaders(200, dados.length);
            try (var saida = troca.getResponseBody()) { saida.write(dados); }
        });
        servidor.start();
        try {
            var importar = new ImportadorOsm(sincronizacao, "-23.57,-46.66,-23.54,-46.63", 5,
                    URI.create("http://127.0.0.1:" + servidor.getAddress().getPort()));
            importar.run();
            long id = banco.queryForObject("SELECT id_estacionamento FROM tb_estacionamento WHERE ds_id_osm = 'way/42'", Long.class);
            String favorito = "/favoritos/" + cliente.post("/favoritos", Map.of("idEstacionamento", id), 201).get("id").asLong();
            corpo.set(corpo.get().replace("Teste OSM", "Nome atualizado"));
            Path arquivo = temporario.resolve("estacionamentos.json");
            Files.writeString(arquivo, corpo.get());
            importar.run("--osm-arquivo=" + arquivo);
            var atualizado = cliente.get("/estacionamentos/" + id, 200);
            assertEquals("Nome atualizado", atualizado.get("nome").asString());
            assertEquals("https://www.openstreetmap.org/way/42", atualizado.get("urlOsm").asString());
            var proximos = cliente.get("/estacionamentos/proximos?latitude=0&longitude=0&raioKm=0.01&tipo=PRIVADO", 200);
            assertEquals(2, proximos.size());
            assertTrue(proximos.get(0).get("preco").isNull());
            cliente.delete("/estacionamentos/" + id, 403);
            cliente.get(favorito, 200);
            cliente.delete(favorito, 204);
            assertEquals(2, banco.queryForObject("SELECT COUNT(*) FROM tb_estacionamento WHERE ds_id_osm IS NOT NULL", Integer.class));
        } finally {
            servidor.stop(0);
        }
    }
}
