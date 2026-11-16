package br.com.fiap.vagai.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

public class EstacionamentoEntrada {
    @NotBlank @Size(max = 100)
    private String nome;
    @Size(max = 255)
    private String descricao;
    @NotNull @Pattern(regexp = "GRATUITO|ROTATIVO|PRIVADO")
    private String tipo;
    @NotNull @DecimalMin("-90") @DecimalMax("90") @Digits(integer = 3, fraction = 7)
    private BigDecimal latitude;
    @NotNull @DecimalMin("-180") @DecimalMax("180") @Digits(integer = 3, fraction = 7)
    private BigDecimal longitude;
    @Size(max = 255)
    private String endereco;
    @PositiveOrZero @Digits(integer = 8, fraction = 2)
    private BigDecimal preco;
    @Size(max = 255)
    private String horario;
    @NotNull @Min(0) @Max(1)
    private Integer ativo;

    public EstacionamentoEntrada() {}

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

    public Integer getAtivo() {
        return ativo;
    }

    public void setAtivo(Integer ativo) {
        this.ativo = ativo;
    }
}
