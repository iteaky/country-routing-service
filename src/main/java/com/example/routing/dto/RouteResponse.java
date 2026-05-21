package com.example.routing.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Response containing the calculated land route")
public record RouteResponse(
        @Schema(description = "Ordered list of country cca3 codes from origin to destination", example = "[\"CZE\", \"AUT\", \"ITA\"]")
        List<String> route
) {
}
