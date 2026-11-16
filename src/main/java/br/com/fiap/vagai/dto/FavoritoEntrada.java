package br.com.fiap.vagai.dto;

import jakarta.validation.constraints.*;

public class FavoritoEntrada {
    @NotNull @Positive
    private Long idEstacionamento;

    public FavoritoEntrada() {}

    public Long getIdEstacionamento() {
        return idEstacionamento;
    }

    public void setIdEstacionamento(Long idEstacionamento) {
        this.idEstacionamento = idEstacionamento;
    }
}
