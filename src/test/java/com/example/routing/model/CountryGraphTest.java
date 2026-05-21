package com.example.routing.model;

import com.example.routing.dto.CountryDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CountryGraphTest {

    @Test
    void shouldFindShortestLandRoute() {
        CountryGraph graph = CountryGraph.fromCountries(List.of(
                new CountryDto("CZE", List.of("AUT", "DEU", "POL", "SVK")),
                new CountryDto("AUT", List.of("CZE", "ITA")),
                new CountryDto("ITA", List.of("AUT")),
                new CountryDto("DEU", List.of("CZE")),
                new CountryDto("POL", List.of("CZE")),
                new CountryDto("SVK", List.of("CZE"))
        ));

        assertThat(graph.findShortestRoute("CZE", "ITA"))
                .containsExactly("CZE", "AUT", "ITA");
    }

    @Test
    void shouldReturnSingleCountryWhenOriginAndDestinationAreTheSame() {
        CountryGraph graph = CountryGraph.fromCountries(List.of(
                new CountryDto("CZE", List.of("AUT")),
                new CountryDto("AUT", List.of("CZE"))
        ));

        assertThat(graph.findShortestRoute("cze", "CZE"))
                .containsExactly("CZE");
    }

    @Test
    void shouldReturnEmptyRouteWhenNoLandRouteExists() {
        CountryGraph graph = CountryGraph.fromCountries(List.of(
                new CountryDto("CZE", List.of("AUT")),
                new CountryDto("AUT", List.of("CZE")),
                new CountryDto("USA", List.of("CAN")),
                new CountryDto("CAN", List.of("USA"))
        ));

        assertThat(graph.findShortestRoute("CZE", "USA"))
                .isEmpty();
    }

    @Test
    void shouldHandleCountriesWithoutBorders() {
        CountryGraph graph = CountryGraph.fromCountries(List.of(
                new CountryDto("ISL", null),
                new CountryDto("CZE", List.of("AUT")),
                new CountryDto("AUT", List.of("CZE"))
        ));

        assertThat(graph.containsCountry("ISL")).isTrue();
        assertThat(graph.findShortestRoute("ISL", "CZE")).isEmpty();
    }
}
