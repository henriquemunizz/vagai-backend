package br.com.fiap.vagai.dto;

import java.util.List;

public class ProximoSaida extends EstacionamentoSaida {
    private double distanciaKm;
    private Double mediaAvaliacoes;
    private List<RegraSaida> regras;
    private ReporteSaida ultimoReporteRecente;

    public ProximoSaida() {}

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public Double getMediaAvaliacoes() {
        return mediaAvaliacoes;
    }

    public void setMediaAvaliacoes(Double mediaAvaliacoes) {
        this.mediaAvaliacoes = mediaAvaliacoes;
    }

    public List<RegraSaida> getRegras() {
        return regras;
    }

    public void setRegras(List<RegraSaida> regras) {
        this.regras = regras;
    }

    public ReporteSaida getUltimoReporteRecente() {
        return ultimoReporteRecente;
    }

    public void setUltimoReporteRecente(ReporteSaida ultimoReporteRecente) {
        this.ultimoReporteRecente = ultimoReporteRecente;
    }
}
