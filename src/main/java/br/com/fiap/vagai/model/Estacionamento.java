package br.com.fiap.vagai.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Estacionamento {
    private Long id;
    private Usuario criador;
    private String nome;
    private String descricao;
    private String tipo;
    private String fonte;
    private String idOsm;
    private LocalDateTime verificacao;
    private Integer ativo;

    private String rua;
    private String bairro;
    private String numero;
    private BigDecimal primeiraHora;
    private BigDecimal demaisHoras;

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

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public BigDecimal getPrimeiraHora() {
        return primeiraHora;
    }

    public void setPrimeiraHora(BigDecimal primeiraHora) {
        this.primeiraHora = primeiraHora;
    }

    public BigDecimal getDemaisHoras() {
        return demaisHoras;
    }

    public void setDemaisHoras(BigDecimal demaisHoras) {
        this.demaisHoras = demaisHoras;
    }
}
