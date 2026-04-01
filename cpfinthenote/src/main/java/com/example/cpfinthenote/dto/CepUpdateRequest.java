package com.example.cpfinthenote.dto;

public record CepUpdateRequest(
        String logradouro,
        String complemento,
        String bairro,
        Integer municipioId,
        String municipio,
        String uf
) {
}
