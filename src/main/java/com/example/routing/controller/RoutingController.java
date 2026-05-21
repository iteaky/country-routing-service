package com.example.routing.controller;

import com.example.routing.dto.RouteResponse;
import com.example.routing.service.CountryRouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/routing")
@Tag(name = "Routing", description = "Calculate possible land routes between countries")
public class RoutingController {

    private final CountryRouteService countryRouteService;

    public RoutingController(CountryRouteService countryRouteService) {
        this.countryRouteService = countryRouteService;
    }

    @Operation(
            summary = "Get a land route between two countries",
            description = "Returns a single possible land route from origin to destination. Countries are identified by their 3-letter cca3 codes."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Land route found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RouteResponse.class),
                            examples = @ExampleObject(value = "{\"route\":[\"CZE\",\"AUT\",\"ITA\"]}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "No land route found or unknown country code",
                    content = @Content
            )
    })
    @GetMapping("/{origin}/{destination}")
    public RouteResponse getRoute(
            @Parameter(description = "Origin country cca3 code", example = "CZE", required = true)
            @PathVariable String origin,
            @Parameter(description = "Destination country cca3 code", example = "ITA", required = true)
            @PathVariable String destination) {
        return new RouteResponse(countryRouteService.findRoute(origin, destination));
    }
}
