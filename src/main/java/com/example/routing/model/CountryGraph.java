package com.example.routing.model;

import com.example.routing.dto.CountryDto;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

public class CountryGraph {

    private final Map<String, Set<String>> adjacencyByCountryCode;

    private CountryGraph(Map<String, Set<String>> adjacencyByCountryCode) {
        this.adjacencyByCountryCode = adjacencyByCountryCode;
    }

    public static CountryGraph fromCountries(Collection<CountryDto> countries) {
        Map<String, Set<String>> adjacency = new LinkedHashMap<>();

        countries.stream()
                .map(CountryDto::cca3)
                .filter(Objects::nonNull)
                .map(CountryGraph::normalize)
                .forEach(code -> adjacency.putIfAbsent(code, new TreeSet<>()));

        for (CountryDto country : countries) {
            if (country.cca3() == null) {
                continue;
            }

            String countryCode = normalize(country.cca3());
            List<String> borders = country.borders() == null ? List.of() : country.borders();

            for (String border : borders) {
                if (border == null) {
                    continue;
                }

                String borderCode = normalize(border);

                if (!adjacency.containsKey(borderCode)) {
                    continue;
                }

                adjacency.get(countryCode).add(borderCode);
                adjacency.get(borderCode).add(countryCode);
            }
        }

        Map<String, Set<String>> immutableAdjacency = new LinkedHashMap<>();
        adjacency.forEach((countryCode, borders) -> immutableAdjacency.put(
                countryCode,
                Collections.unmodifiableSet(new TreeSet<>(borders))
        ));

        return new CountryGraph(Collections.unmodifiableMap(immutableAdjacency));
    }

    public boolean containsCountry(String countryCode) {
        return adjacencyByCountryCode.containsKey(normalize(countryCode));
    }

    public List<String> findShortestRoute(String origin, String destination) {
        String normalizedOrigin = normalize(origin);
        String normalizedDestination = normalize(destination);

        if (normalizedOrigin.equals(normalizedDestination)) {
            return List.of(normalizedOrigin);
        }

        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> previousByCountryCode = new HashMap<>();

        queue.add(normalizedOrigin);
        visited.add(normalizedOrigin);

        while (!queue.isEmpty()) {
            String currentCountry = queue.poll();

            for (String nextCountry : adjacencyByCountryCode.getOrDefault(currentCountry, Set.of())) {
                if (!visited.add(nextCountry)) {
                    continue;
                }

                previousByCountryCode.put(nextCountry, currentCountry);

                if (nextCountry.equals(normalizedDestination)) {
                    return buildRoute(normalizedOrigin, normalizedDestination, previousByCountryCode);
                }

                queue.add(nextCountry);
            }
        }

        return List.of();
    }

    private static List<String> buildRoute(String origin,
                                           String destination,
                                           Map<String, String> previousByCountryCode) {
        List<String> route = new ArrayList<>();
        String current = destination;

        while (current != null) {
            route.add(current);

            if (current.equals(origin)) {
                break;
            }

            current = previousByCountryCode.get(current);
        }

        Collections.reverse(route);
        return route;
    }

    private static String normalize(String countryCode) {
        return countryCode.trim().toUpperCase();
    }
}
