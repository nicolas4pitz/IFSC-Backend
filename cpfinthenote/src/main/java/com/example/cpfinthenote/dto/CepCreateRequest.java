package com.example.cpfinthenote.dto;

import jakarta.validation.constraints.NotBlank;

public record CepCreateRequest(
        @NotBlank String cep,
        @NotBlank String logradouro,
        String complemento,
        @NotBlank String bairro,
        Integer municipioId,
        String municipio,
        String uf
) {
}
