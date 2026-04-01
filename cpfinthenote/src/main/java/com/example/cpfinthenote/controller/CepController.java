package com.example.cpfinthenote.controller;

import com.example.cpfinthenote.dto.CepCreateRequest;
import com.example.cpfinthenote.dto.CepResponse;
import com.example.cpfinthenote.dto.CepUpdateRequest;
import com.example.cpfinthenote.service.CepService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ceps")
public class CepController {

    private final CepService cepService;

    public CepController(CepService cepService) {
        this.cepService = cepService;
    }

    @GetMapping("/estado/{estado}")
    public List<CepResponse> getByState(@PathVariable String estado) {
        return cepService.getCepsByState(estado);
    }

    @GetMapping("/municipio/{municipio}")
    public List<CepResponse> getByCity(@PathVariable String municipio) {
        return cepService.getCepsByCity(municipio);
    }

    @GetMapping("/municipio/{municipio}/filtro")
    public List<CepResponse> getByCityWithFilter(
            @PathVariable String municipio,
            @RequestParam String termo
    ) {
        return cepService.getCepsByCityWithFilter(municipio, termo);
    }

    @GetMapping("/{cep}")
    public CepResponse getByCep(@PathVariable String cep) {
        return cepService.getByCep(cep);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CepResponse create(@Valid @RequestBody CepCreateRequest request) {
        return cepService.create(request);
    }

    @PutMapping("/{cep}")
    public CepResponse update(@PathVariable String cep, @RequestBody CepUpdateRequest request) {
        return cepService.update(cep, request);
    }
}
