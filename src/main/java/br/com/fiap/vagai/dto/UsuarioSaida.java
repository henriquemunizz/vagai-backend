package br.com.fiap.vagai.dto;

import java.time.LocalDateTime;

public class UsuarioSaida {
    private Long id;
    private String nome;
    private LocalDateTime cadastro;

    public UsuarioSaida() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDateTime getCadastro() {
        return cadastro;
    }

    public void setCadastro(LocalDateTime cadastro) {
        this.cadastro = cadastro;
    }
}
