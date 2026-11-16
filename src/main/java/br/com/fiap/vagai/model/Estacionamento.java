package br.com.fiap.vagai.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Estacionamento {
    private Long id;
    private Usuario criador;
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

    public Estacionamento() {}

    public Estacionamento(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getCriador() {
        return criador;
    }

    public void setCriador(Usuario criador) {
        this.criador = criador;
    }

    public Long getIdUsuarioCriador() {
        return criador == null ? null : criador.getId();
    }

    public void setIdUsuarioCriador(Long id) {
        criador = id == null ? null : new Usuario(id);
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
