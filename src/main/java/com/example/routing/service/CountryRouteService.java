package com.example.routing.service;

import com.example.routing.client.CountryDataClient;
import com.example.routing.exception.NoLandRouteException;
import com.example.routing.model.CountryGraph;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryRouteService {

    private final CountryDataClient countryDataClient;
    private CountryGraph countryGraph;

    public CountryRouteService(CountryDataClient countryDataClient) {
        this.countryDataClient = countryDataClient;
    }

    @PostConstruct
    void loadCountryGraph() {
        this.countryGraph = CountryGraph.fromCountries(countryDataClient.fetchCountries());
    }

    public List<String> findRoute(String origin, String destination) {
        validateCountryCode(origin, "origin");
        validateCountryCode(destination, "destination");

        String normalizedOrigin = normalize(origin);
        String normalizedDestination = normalize(destination);

        if (!countryGraph.containsCountry(normalizedOrigin)) {
            throw new NoLandRouteException("Unknown origin country code: " + normalizedOrigin);
        }

        if (!countryGraph.containsCountry(normalizedDestination)) {
            throw new NoLandRouteException("Unknown destination country code: " + normalizedDestination);
        }

        List<String> route = countryGraph.findShortestRoute(normalizedOrigin, normalizedDestination);

        if (route.isEmpty()) {
            throw new NoLandRouteException("No land route found from " + normalizedOrigin + " to " + normalizedDestination);
        }

        return route;
    }

    private static void validateCountryCode(String countryCode, String fieldName) {
        if (countryCode == null || countryCode.isBlank()) {
            throw new NoLandRouteException("Missing " + fieldName + " country code");
        }

        if (countryCode.trim().length() != 3) {
            throw new NoLandRouteException("Country code must be a 3-letter cca3 code");
        }
    }

    private static String normalize(String countryCode) {
        return countryCode.trim().toUpperCase();
    }
}
