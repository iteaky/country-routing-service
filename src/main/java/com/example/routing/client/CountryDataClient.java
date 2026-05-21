package com.example.routing.client;

import com.example.routing.dto.CountryDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class CountryDataClient {

    private static final TypeReference<List<CountryDto>> COUNTRY_LIST_TYPE = new TypeReference<>() {
    };

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String countryDataUrl;

    public CountryDataClient(RestClient.Builder restClientBuilder,
                             ObjectMapper objectMapper,
                             @Value("${countries.data-url}") String countryDataUrl) {
        this.restClient = restClientBuilder.build();
        this.objectMapper = objectMapper;
        this.countryDataUrl = countryDataUrl;
    }

    public List<CountryDto> fetchCountries() {
        String responseBody = restClient.get()
                .uri(countryDataUrl)
                .retrieve()
                .body(String.class);

        if (responseBody == null || responseBody.isBlank()) {
            throw new IllegalStateException("Country data source returned an empty response");
        }

        List<CountryDto> countries = parseCountries(responseBody);

        if (countries.isEmpty()) {
            throw new IllegalStateException("Country data source returned no countries");
        }

        return countries;
    }

    private List<CountryDto> parseCountries(String responseBody) {
        try {
            return objectMapper.readValue(responseBody, COUNTRY_LIST_TYPE);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to parse country data source response", e);
        }
    }
}
