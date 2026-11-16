package br.com.fiap.vagai.dto;

import jakarta.validation.constraints.*;

public class RegraEntrada {
    @NotNull @Positive
    private Long idEstacionamento;
    @NotBlank @Size(max = 255)
    private String descricao;
    @Size(max = 50)
    private String diaSemana;
    @Pattern(regexp = "(?:[01][0-9]|2[0-3]):[0-5][0-9]")
    private String inicio;
    @Pattern(regexp = "(?:[01][0-9]|2[0-3]):[0-5][0-9]")
    private String fim;
    @NotNull @Min(0) @Max(1)
    private Integer ativo;

    public RegraEntrada() {}

    public Long getIdEstacionamento() {
        return idEstacionamento;
    }

    public void setIdEstacionamento(Long idEstacionamento) {
        this.idEstacionamento = idEstacionamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFim() {
        return fim;
    }

    public void setFim(String fim) {
        this.fim = fim;
    }

    public Integer getAtivo() {
        return ativo;
    }

    public void setAtivo(Integer ativo) {
        this.ativo = ativo;
    }
}
