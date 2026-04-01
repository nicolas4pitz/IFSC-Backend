package com.example.cpfinthenote.dto;

public record CepResponse(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        int municipioId,
        String municipio,
        int estadoId,
        String estado,
        String uf
) {
}
