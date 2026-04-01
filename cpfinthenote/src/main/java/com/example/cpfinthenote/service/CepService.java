package com.example.cpfinthenote.service;

import com.example.cpfinthenote.dto.CepCreateRequest;
import com.example.cpfinthenote.dto.CepResponse;
import com.example.cpfinthenote.dto.CepUpdateRequest;
import com.example.cpfinthenote.model.CepData;
import com.example.cpfinthenote.model.CityData;
import com.example.cpfinthenote.model.StateData;
import jakarta.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class CepService {

    private static final Pattern NON_DIGITS = Pattern.compile("\\D");

    private final Map<Integer, StateData> statesById = new HashMap<>();
    private final Map<String, StateData> statesByNormalizedKey = new HashMap<>();

    private final Map<Integer, CityData> citiesById = new HashMap<>();
    private final Map<String, List<CityData>> citiesByNormalizedName = new HashMap<>();

    private final Map<String, CepData> cepsByCode = new HashMap<>();
    private final Map<Integer, Set<String>> cepCodesByStateId = new HashMap<>();
    private final Map<Integer, Set<String>> cepCodesByCityId = new HashMap<>();

    @Value("${cep.data-dir:../cep-sc}")
    private String dataDir;

    @PostConstruct
    public void init() {
        loadAllData();
    }

    public List<CepResponse> getCepsByState(String stateIdentifier) {
        StateData state = resolveState(stateIdentifier);
        return responseListFromCodes(cepCodesByStateId.getOrDefault(state.id(), Set.of()));
    }

    public List<CepResponse> getCepsByCity(String cityIdentifier) {
        CityData city = resolveCity(cityIdentifier, null);
        return responseListFromCodes(cepCodesByCityId.getOrDefault(city.id(), Set.of()));
    }

    public List<CepResponse> getCepsByCityWithFilter(String cityIdentifier, String term) {
        CityData city = resolveCity(cityIdentifier, null);
        String normalizedTerm = normalize(term);

        return responseListFromCodes(cepCodesByCityId.getOrDefault(city.id(), Set.of())).stream()
                .filter(item -> normalize(item.logradouro()).contains(normalizedTerm)
                        || normalize(item.complemento()).contains(normalizedTerm)
                        || normalize(item.bairro()).contains(normalizedTerm)
                        || normalize(item.municipio()).contains(normalizedTerm))
                .toList();
    }

    public CepResponse getByCep(String cep) {
        String normalizedCep = normalizeCep(cep);
        CepData data = cepsByCode.get(normalizedCep);
        if (data == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CEP nao encontrado");
        }
        return toResponse(data);
    }

    public CepResponse create(CepCreateRequest request) {
        String normalizedCep = normalizeCep(request.cep());
        if (cepsByCode.containsKey(normalizedCep)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CEP ja cadastrado");
        }

        CityData city = resolveCity(request.municipioId(), request.municipio(), request.uf());
        StateData state = statesById.get(city.stateId());

        CepData data = new CepData(
                normalizedCep,
                request.logradouro().trim(),
                nullToEmpty(request.complemento()).trim(),
                request.bairro().trim(),
                city.id(),
                state.id()
        );

        upsertCep(data);
        return toResponse(data);
    }

    public CepResponse update(String cep, CepUpdateRequest request) {
        String normalizedCep = normalizeCep(cep);
        CepData existing = cepsByCode.get(normalizedCep);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CEP nao encontrado");
        }

        CityData city = existingCityOrResolved(request, existing);
        StateData state = statesById.get(city.stateId());

        CepData updated = new CepData(
                normalizedCep,
                request.logradouro() != null ? request.logradouro().trim() : existing.street(),
                request.complemento() != null ? request.complemento().trim() : existing.complement(),
                request.bairro() != null ? request.bairro().trim() : existing.neighborhood(),
                city.id(),
                state.id()
        );

        removeIndexes(existing);
        upsertCep(updated);
        return toResponse(updated);
    }

    private CityData existingCityOrResolved(CepUpdateRequest request, CepData existing) {
        if (request.municipioId() == null && request.municipio() == null && request.uf() == null) {
            return citiesById.get(existing.cityId());
        }
        return resolveCity(request.municipioId(), request.municipio(), request.uf());
    }

    private void loadAllData() {
        Path root = resolveDataDirectory();
        if (!Files.isDirectory(root)) {
            throw new IllegalStateException("Diretorio de dados nao encontrado: " + root);
        }

        loadStates(root.resolve("states.csv"));
        loadCities(root.resolve("cities.csv"));
        loadCepFiles(root);
    }

    private Path resolveDataDirectory() {
        List<Path> candidates = List.of(
                Paths.get(dataDir),
                Paths.get("../cep-sc"),
                Paths.get("../../cep-sc"),
                Paths.get("/home/aluno/Downloads/cep-sc")
        );

        for (Path candidate : candidates) {
            Path normalized = candidate.toAbsolutePath().normalize();
            if (Files.isDirectory(normalized)) {
                return normalized;
            }
        }

        return Paths.get(dataDir).toAbsolutePath().normalize();
    }

    private void loadStates(Path file) {
        readCsv(file).forEach(parts -> {
            int id = Integer.parseInt(parts.get(0));
            StateData state = new StateData(id, parts.get(1), parts.get(2));
            statesById.put(id, state);
            statesByNormalizedKey.put(normalize(parts.get(1)), state);
            statesByNormalizedKey.put(normalize(parts.get(2)), state);
            statesByNormalizedKey.put(String.valueOf(id), state);
        });
    }

    private void loadCities(Path file) {
        readCsv(file).forEach(parts -> {
            int id = Integer.parseInt(parts.get(0));
            CityData city = new CityData(id, parts.get(1), Integer.parseInt(parts.get(2)));
            citiesById.put(id, city);
            citiesByNormalizedName.computeIfAbsent(normalize(city.name()), ignored -> new ArrayList<>()).add(city);
        });
    }

    private void loadCepFiles(Path root) {
        try (Stream<Path> files = Files.list(root)) {
            files
                .filter(path -> path.getFileName().toString().startsWith("sc.cepaberto_parte_"))
                .filter(path -> path.getFileName().toString().endsWith(".csv"))
                .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                .forEach(this::loadCepFile);
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao listar arquivos de CEP", e);
        }
    }

    private void loadCepFile(Path file) {
        readCsv(file).forEach(parts -> {
            CepData data = new CepData(
                    normalizeCep(parts.get(0)),
                    parts.get(1),
                    parts.get(2),
                    parts.get(3),
                    Integer.parseInt(parts.get(4)),
                    Integer.parseInt(parts.get(5))
            );
            upsertCep(data);
        });
    }

    private List<CSVRecord> readCsv(Path file) {
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT.parse(reader)) {
            return parser.getRecords();
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao ler arquivo: " + file, e);
        }
    }

    private void upsertCep(CepData data) {
        cepsByCode.put(data.cep(), data);
        cepCodesByStateId.computeIfAbsent(data.stateId(), ignored -> new HashSet<>()).add(data.cep());
        cepCodesByCityId.computeIfAbsent(data.cityId(), ignored -> new HashSet<>()).add(data.cep());
    }

    private void removeIndexes(CepData data) {
        Set<String> stateCodes = cepCodesByStateId.get(data.stateId());
        if (stateCodes != null) {
            stateCodes.remove(data.cep());
        }
        Set<String> cityCodes = cepCodesByCityId.get(data.cityId());
        if (cityCodes != null) {
            cityCodes.remove(data.cep());
        }
    }

    private List<CepResponse> responseListFromCodes(Set<String> cepCodes) {
        return cepCodes.stream()
                .map(cepsByCode::get)
                .sorted(Comparator.comparing(CepData::cep))
                .map(this::toResponse)
                .toList();
    }

    private CepResponse toResponse(CepData data) {
        CityData city = citiesById.get(data.cityId());
        StateData state = statesById.get(data.stateId());
        return new CepResponse(
                data.cep(),
                data.street(),
                data.complement(),
                data.neighborhood(),
                city.id(),
                city.name(),
                state.id(),
                state.name(),
                state.uf()
        );
    }

    private StateData resolveState(String identifier) {
        StateData state = statesByNormalizedKey.get(normalize(identifier));
        if (state == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado nao encontrado");
        }
        return state;
    }

    private CityData resolveCity(String cityIdentifier, String stateIdentifier) {
        if (isInteger(cityIdentifier)) {
            CityData city = citiesById.get(Integer.parseInt(cityIdentifier));
            if (city == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Municipio nao encontrado");
            }
            return city;
        }

        List<CityData> cities = citiesByNormalizedName.getOrDefault(normalize(cityIdentifier), List.of());
        if (cities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Municipio nao encontrado");
        }

        if (stateIdentifier == null || stateIdentifier.isBlank()) {
            if (cities.size() > 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Municipio ambiguo. Informe tambem o estado (uf).");
            }
            return cities.getFirst();
        }

        StateData state = resolveState(stateIdentifier);
        return cities.stream()
                .filter(city -> city.stateId() == state.id())
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Municipio nao encontrado para o estado informado"));
    }

    private CityData resolveCity(Integer cityId, String cityName, String stateIdentifier) {
        if (cityId != null) {
            CityData city = citiesById.get(cityId);
            if (city == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Municipio nao encontrado");
            }
            return city;
        }

        if (cityName == null || cityName.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Informe municipioId ou municipio + uf");
        }

        return resolveCity(cityName, stateIdentifier);
    }

    private static String normalizeCep(String cep) {
        String onlyDigits = NON_DIGITS.matcher(cep).replaceAll("");
        if (onlyDigits.length() != 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CEP invalido. Use 8 digitos");
        }
        return onlyDigits;
    }

    private static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    private static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .trim();
        return normalized.replaceAll("\\s+", " ");
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
