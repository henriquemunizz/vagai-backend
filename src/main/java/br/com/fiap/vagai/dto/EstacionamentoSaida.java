package br.com.fiap.vagai.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EstacionamentoSaida {
    private Long id;
    private Long idUsuarioCriador;
    private String nome;
    private String descricao;
    private String tipo;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String endereco;
    private BigDecimal preco;
    private String horario;
    private String fonte;
    private String idOsm;
    private LocalDateTime verificacao;
    private Integer ativo;

    public EstacionamentoSaida() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuarioCriador() {
        return idUsuarioCriador;
    }

    public void setIdUsuarioCriador(Long idUsuarioCriador) {
        this.idUsuarioCriador = idUsuarioCriador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getFonte() {
        return fonte;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }

    public String getIdOsm() {
        return idOsm;
    }

    public String getUrlOsm() {
        return idOsm == null ? null : "https://www.openstreetmap.org/" + idOsm;
    }

    public void setIdOsm(String idOsm) {
        this.idOsm = idOsm;
    }

    public LocalDateTime getVerificacao() {
        return verificacao;
    }

    public void setVerificacao(LocalDateTime verificacao) {
        this.verificacao = verificacao;
    }

    public Integer getAtivo() {
        return ativo;
    }

    public void setAtivo(Integer ativo) {
        this.ativo = ativo;
    }
}
