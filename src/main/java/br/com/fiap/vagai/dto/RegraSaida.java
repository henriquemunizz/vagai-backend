package br.com.fiap.vagai.dto;

public class RegraSaida {
    private Long id;
    private Long idEstacionamento;
    private String descricao;
    private String diaSemana;
    private String inicio;
    private String fim;
    private Integer ativo;

    public RegraSaida() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
