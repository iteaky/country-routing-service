package com.example.routing.client;

import com.example.routing.dto.CountryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class CountryDataClientTest {

    @Test
    void shouldParseCountryDataEvenWhenSourceReturnsTextPlainContentType() {
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        CountryDataClient client = new CountryDataClient(
                restClientBuilder,
                new ObjectMapper(),
                "https://example.test/countries.json"
        );

        server.expect(requestTo("https://example.test/countries.json"))
                .andRespond(withSuccess("""
                        [
                          {"cca3":"CZE","borders":["AUT","DEU"],"ignoredField":"ignored"},
                          {"cca3":"AUT","borders":["CZE","ITA"]}
                        ]
                        """, MediaType.TEXT_PLAIN));

        List<CountryDto> countries = client.fetchCountries();

        assertThat(countries)
                .extracting(CountryDto::cca3)
                .containsExactly("CZE", "AUT");
        assertThat(countries.get(0).borders())
                .containsExactly("AUT", "DEU");

        server.verify();
    }
}
