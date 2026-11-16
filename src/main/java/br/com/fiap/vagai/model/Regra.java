package br.com.fiap.vagai.model;

public class Regra {
    private Long id;
    private Estacionamento estacionamento;
    private String descricao;
    private String diaSemana;
    private String inicio;
    private String fim;
    private Integer ativo;

    public Regra() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Estacionamento getEstacionamento() {
        return estacionamento;
    }

    public void setEstacionamento(Estacionamento estacionamento) {
        this.estacionamento = estacionamento;
    }

    public Long getIdEstacionamento() {
        return estacionamento == null ? null : estacionamento.getId();
    }

    public void setIdEstacionamento(Long id) {
        estacionamento = id == null ? null : new Estacionamento(id);
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
