package br.com.fiap.vagai.service;

import br.com.fiap.vagai.dao.*;
import br.com.fiap.vagai.dto.*;
import br.com.fiap.vagai.model.Estacionamento;
import br.com.fiap.vagai.exception.EntradaInvalida;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.time.*;
import java.util.*;

@Service
public class BuscaGeografica {
    private final EstacionamentoDao locais;
    private final AvaliacaoDao avaliacoes;
    private final RegraDao regras;
    private final ReporteDao reportes;
    private final ModelMapper mapper;

    public BuscaGeografica(EstacionamentoDao locais, AvaliacaoDao avaliacoes, RegraDao regras,
                           ReporteDao reportes, ModelMapper mapper) {
        this.locais = locais;
        this.avaliacoes = avaliacoes;
        this.regras = regras;
        this.reportes = reportes;
        this.mapper = mapper;
    }

    public List<ProximoSaida> buscar(double latitude, double longitude, double raioKm, String tipo) throws SQLException {
        if (!Double.isFinite(latitude) || Math.abs(latitude) > 90 || !Double.isFinite(longitude) ||
            Math.abs(longitude) > 180 || !Double.isFinite(raioKm) || raioKm <= 0)
            throw new EntradaInvalida("Informe coordenadas válidas e um raio positivo em km.");
        if (tipo != null && !Set.of("GRATUITO", "ROTATIVO", "PRIVADO").contains(tipo))
            throw new EntradaInvalida("Tipo de estacionamento inválido.");
        List<ProximoSaida> resultado = new ArrayList<>();
        LocalDateTime agora = LocalDateTime.now(ZoneOffset.UTC);
        for (Estacionamento local : locais.ativos(tipo)) {
            double km = distancia(latitude, longitude, local.getLatitude().doubleValue(), local.getLongitude().doubleValue());
            if (km > raioKm) continue;
            ProximoSaida item = mapper.map(local, ProximoSaida.class);
            item.setDistanciaKm(km);
            var media = avaliacoes.doLocal(local.getId()).stream().mapToInt(a -> a.getNota()).average();
            item.setMediaAvaliacoes(media.isPresent() ? media.getAsDouble() : null);
            item.setRegras(regras.doLocal(local.getId()).stream().filter(r -> r.getAtivo() == 1)
                    .map(r -> mapper.map(r, RegraSaida.class)).toList());
            if (!"PRIVADO".equals(local.getTipo())) {
                reportes.doLocal(local.getId()).stream().filter(r -> r.atualEm(agora))
                        .max(Comparator.comparing((br.com.fiap.vagai.model.Reporte r) -> r.getData())
                                .thenComparing(r -> r.getId())).ifPresent(r -> {
                            item.setUltimoReporteRecente(mapper.map(r, ReporteSaida.class));
                            item.getUltimoReporteRecente().setAtual(true);
                        });
            }
            resultado.add(item);
        }
        resultado.sort(Comparator.comparingDouble(item -> item.getDistanciaKm()));
        return resultado;
    }

    public static double distancia(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.pow(Math.sin(dLon / 2), 2);
        return 6371.0088 * 2 * Math.asin(Math.sqrt(Math.min(1, a)));
    }
}
