package br.com.fiap.vagai.dto;

import java.math.BigDecimal;

public class CentroOsmResponseDto {
    private BigDecimal lat;
    private BigDecimal lon;

    public CentroOsmResponseDto() {}

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }
}
