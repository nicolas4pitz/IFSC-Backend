package com.example.cpfinthenote;

import com.example.cpfinthenote.dto.CepCreateRequest;
import com.example.cpfinthenote.dto.CepResponse;
import com.example.cpfinthenote.dto.CepUpdateRequest;
import com.example.cpfinthenote.service.CepService;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CpfinthenoteApplicationTests {

    @Autowired
    private CepService cepService;

    @Test
    void shouldGetLocalidadeByCep() throws Exception {
    CepResponse response = cepService.getByCep("88010010");

    assertThat(response.cep()).isEqualTo("88010010");
    assertThat(response.uf()).isEqualTo("SC");
    assertThat(response.municipio()).isNotBlank();
    }

    @Test
    void shouldGetAllCepsByState() throws Exception {
    List<CepResponse> response = cepService.getCepsByState("SC");

    assertThat(response).isNotEmpty();
    assertThat(response.get(0).uf()).isEqualTo("SC");
    }

    @Test
    void shouldFilterByCityAndTerm() throws Exception {
    List<CepResponse> response = cepService.getCepsByCityWithFilter("3454", "Felipe Schmidt");

    assertThat(response).isNotEmpty();
    assertThat(response.get(0).logradouro()).containsIgnoringCase("Felipe");
    }

    @Test
    void shouldCreateAndUpdateCep() throws Exception {
    String generatedCep = UUID.randomUUID().toString().replaceAll("\\D", "");
    generatedCep = (generatedCep + "99999999").substring(0, 8);

    CepResponse created = cepService.create(new CepCreateRequest(
        generatedCep,
        "Rua de Teste",
        "Bloco B",
        "Centro",
        3454,
        null,
        null
    ));

    assertThat(created.cep()).isEqualTo(generatedCep);

    CepResponse updated = cepService.update(generatedCep, new CepUpdateRequest(
        null,
        null,
        "Centro Atualizado",
        null,
        null,
        null
    ));

    assertThat(updated.bairro()).isEqualTo("Centro Atualizado");

    CepResponse retrieved = cepService.getByCep(generatedCep);
    assertThat(retrieved.bairro()).isEqualTo("Centro Atualizado");
    }

}
