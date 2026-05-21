package com.example.routing.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI countryRoutingOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Country Routing Service API")
                        .version("v1")
                        .description("REST API for calculating possible land routes between countries by using cca3 country codes and border data.")
                        .contact(new Contact().name("Backend Developer Test")));
    }
}
