package br.com.fiap.vagai.dto;

import jakarta.validation.constraints.*;

public record LoginEntrada(@NotBlank @Size(max = 40) String nome,
                          @NotBlank @Size(max = 255) String senha) {}
