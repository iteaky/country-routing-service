package com.example.routing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CountryDto(
        String cca3,
        List<String> borders
) {
}
