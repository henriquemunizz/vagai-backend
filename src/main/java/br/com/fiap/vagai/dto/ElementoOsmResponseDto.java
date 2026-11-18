package br.com.fiap.vagai.dto;

import java.math.BigDecimal;
import java.util.Map;

public class ElementoOsmResponseDto {
    private String type;
    private long id;
    private BigDecimal lat;
    private BigDecimal lon;
    private CentroOsmResponseDto center;
    private Map<String, String> tags;

    public ElementoOsmResponseDto() {}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public CentroOsmResponseDto getCenter() {
        return center;
    }

    public void setCenter(CentroOsmResponseDto center) {
        this.center = center;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }
}
