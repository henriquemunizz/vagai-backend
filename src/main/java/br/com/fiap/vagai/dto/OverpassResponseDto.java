package br.com.fiap.vagai.dto;

import java.util.List;

public class OverpassResponseDto {
    private List<ElementoOsmResponseDto> elements;
    private String remark;

    public OverpassResponseDto() {}

    public List<ElementoOsmResponseDto> getElements() {
        return elements;
    }

    public void setElements(List<ElementoOsmResponseDto> elements) {
        this.elements = elements;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
