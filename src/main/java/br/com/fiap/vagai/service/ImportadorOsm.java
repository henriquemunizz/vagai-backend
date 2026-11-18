package br.com.fiap.vagai.service;

import br.com.fiap.vagai.model.Estacionamento;
import br.com.fiap.vagai.dto.ElementoOsmResponseDto;
import br.com.fiap.vagai.dto.OverpassResponseDto;
import br.com.fiap.vagai.bo.EstacionamentoOsmBo;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;

@Component
@Profile("importar")
public class ImportadorOsm implements CommandLineRunner {

    private final EstacionamentoOsmBo sincronizacao;
    private final String bbox;
    private final int timeout;
    private final URI endpoint;

    public ImportadorOsm(EstacionamentoOsmBo sincronizacao, @Value("${vagai.osm.bbox}") String bbox,
                         @Value("${vagai.osm.timeout}") int timeout, @Value("${vagai.osm.url}") URI endpoint) {
        this.sincronizacao = sincronizacao;
        this.bbox = bbox;
        this.timeout = timeout;
        this.endpoint = endpoint;
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> arquivos = new DefaultApplicationArguments(args).getOptionValues("osm-arquivo");
        if (arquivos != null && (arquivos.size() != 1 || arquivos.getFirst().isBlank()))
            throw new IllegalArgumentException("Informe --osm-arquivo=caminho.json");
        String json = arquivos == null ? consultarOverpass() : Files.readString(Path.of(arquivos.getFirst()));
        OverpassResponseDto resposta = new ObjectMapper().readValue(json, OverpassResponseDto.class);
        if (resposta.getElements() == null || resposta.getRemark() != null)
            throw new IllegalStateException("Resposta Overpass incompleta.");
        int aceitos = 0;
        for (ElementoOsmResponseDto e : resposta.getElements()) {
            if (e.getType() == null || !Set.of("node", "way", "relation").contains(e.getType()) || e.getId() <= 0) continue;
            String chave = e.getType() + "/" + e.getId();
            Estacionamento local = converter(e, chave);
            if (local == null) sincronizacao.suspender(chave);
            else {
                sincronizacao.salvar(local);
                aceitos++;
            }
        }
        System.out.printf("Recebidos %d elementos. Classificados %d.%n", resposta.getElements().size(), aceitos);
    }

    private String consultarOverpass() throws Exception {
        if (timeout < 1 || timeout > 120) throw new IllegalArgumentException("Timeout deve ser de 1 a 120 segundos.");
        String[] limites = bbox.split(",", -1);
        if (limites.length != 4) throw new IllegalArgumentException("Use sul,oeste,norte,leste.");
        double[] box = Arrays.stream(limites).mapToDouble(Double::parseDouble).toArray();
        if (!Arrays.stream(box).allMatch(Double::isFinite) || box[0] < -90 || box[2] > 90 ||
            box[1] < -180 || box[3] > 180 || box[0] >= box[2] || box[1] >= box[3] ||
            box[2] - box[0] > 0.25 || box[3] - box[1] > 0.25)
            throw new IllegalArgumentException("Região inválida ou maior que 0,25 grau por lado.");
        String area = String.join(",", Arrays.stream(box).mapToObj(Double::toString).toList());
        String query = "[out:json][timeout:" + timeout + "];nwr[amenity=parking](" + area + ");out center;";
        Duration espera = Duration.ofSeconds(timeout + 10L);
        var cliente = HttpClient.newBuilder().connectTimeout(espera).build();
        URI consulta = URI.create(endpoint + "?data=" + URLEncoder.encode(query, StandardCharsets.UTF_8));
        var pedido = HttpRequest.newBuilder(consulta).timeout(espera)
                .header("User-Agent", "Vagai-FIAP/1.0").GET().build();
        var http = cliente.send(pedido, HttpResponse.BodyHandlers.ofString());
        if (http.statusCode() != 200) throw new IllegalStateException("Overpass HTTP " + http.statusCode() + ". Tente mais tarde.");
        return http.body();
    }

    static Estacionamento converter(ElementoOsmResponseDto e, String chave) {
        Map<String, String> tags = e.getTags();
        if (tags == null || !"parking".equals(tags.get("amenity"))) return null;
        // O tipo do operador indica a categoria. A tarifa é uma informação separada.
        if (!"private".equals(tags.get("operator:type")) || !"yes".equals(tags.get("access")) ||
            tags.keySet().stream().anyMatch(k -> k.endsWith(":conditional"))) return null;
        String rua = limitar(tags.get("addr:street"), 150);
        String bairro = limitar(tags.get("addr:suburb"), 100);
        String numero = limitar(tags.get("addr:housenumber"), 20);
        if (rua == null || bairro == null || numero == null) return null;
        Estacionamento local = new Estacionamento();
        local.setIdOsm(chave);
        local.setNome(limitar(tags.getOrDefault("name", "Estacionamento " + chave), 100));
        local.setTipo("PRIVADO");
        local.setFonte("OSM");
        local.setRua(rua);
        local.setBairro(bairro);
        local.setNumero(numero);
        local.setAtivo(1);
        StringJoiner info = new StringJoiner(", ");
        for (String tag : List.of("access", "fee", "capacity", "parking", "charge"))
            if (tags.containsKey(tag)) info.add(tag + "=" + tags.get(tag));
        local.setDescricao(limitar(info.toString(), 255));
        return local;
    }

    private static String limitar(String valor, int tamanho) {
        if (valor == null || valor.isBlank()) return null;
        return valor.substring(0, Math.min(valor.length(), tamanho));
    }
}
