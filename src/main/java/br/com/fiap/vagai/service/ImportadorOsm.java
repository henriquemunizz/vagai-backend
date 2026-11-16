package br.com.fiap.vagai.service;

import br.com.fiap.vagai.model.Estacionamento;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    public record Centro(BigDecimal lat, BigDecimal lon) {}
    public record Elemento(String type, long id, BigDecimal lat, BigDecimal lon, Centro center,
                           Map<String, String> tags) {}
    public record Resposta(List<Elemento> elements, String remark) {}

    private final SincronizacaoOsm sincronizacao;
    private final String bbox;
    private final int timeout;
    private final URI endpoint;

    public ImportadorOsm(SincronizacaoOsm sincronizacao, @Value("${vagai.osm.bbox}") String bbox,
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
        Resposta resposta = new ObjectMapper().readValue(json, Resposta.class);
        if (resposta.elements == null || resposta.remark != null)
            throw new IllegalStateException("Resposta Overpass incompleta.");
        int aceitos = 0;
        for (Elemento e : resposta.elements) {
            if (e.type == null || !Set.of("node", "way", "relation").contains(e.type) || e.id <= 0) continue;
            String chave = e.type + "/" + e.id;
            Estacionamento local = converter(e, chave);
            if (local == null) sincronizacao.suspender(chave);
            else {
                sincronizacao.salvar(local);
                aceitos++;
            }
        }
        System.out.printf("Recebidos %d elementos. Classificados %d.%n", resposta.elements.size(), aceitos);
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

    static Estacionamento converter(Elemento e, String chave) {
        Map<String, String> tags = e.tags;
        if (tags == null || !"parking".equals(tags.get("amenity"))) return null;
        // O tipo do operador indica a categoria. A tarifa é uma informação separada.
        if (!"private".equals(tags.get("operator:type")) || !"yes".equals(tags.get("access")) ||
            tags.keySet().stream().anyMatch(k -> k.endsWith(":conditional"))) return null;
        Centro centro = e.type.equals("node") ? new Centro(e.lat, e.lon) : e.center;
        if (centro == null || centro.lat == null || centro.lon == null ||
            centro.lat.abs().doubleValue() > 90 || centro.lon.abs().doubleValue() > 180) return null;
        Estacionamento local = new Estacionamento();
        local.setIdOsm(chave);
        local.setNome(limitar(tags.getOrDefault("name", "Estacionamento " + chave), 100));
        local.setTipo("PRIVADO");
        local.setFonte("OpenStreetMap");
        local.setLatitude(centro.lat.setScale(7, RoundingMode.HALF_UP));
        local.setLongitude(centro.lon.setScale(7, RoundingMode.HALF_UP));
        local.setAtivo(1);
        local.setHorario(limitar(tags.get("opening_hours"), 255));
        local.setEndereco(limitar(String.join(" ", tags.getOrDefault("addr:street", ""),
                tags.getOrDefault("addr:housenumber", "")).strip(), 255));
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
