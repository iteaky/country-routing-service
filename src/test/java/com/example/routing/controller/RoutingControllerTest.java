package com.example.routing.controller;

import com.example.routing.exception.NoLandRouteException;
import com.example.routing.service.CountryRouteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoutingController.class)
class RoutingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CountryRouteService countryRouteService;

    @Test
    void shouldReturnRoute() throws Exception {
        when(countryRouteService.findRoute("CZE", "ITA"))
                .thenReturn(List.of("CZE", "AUT", "ITA"));

        mockMvc.perform(get("/routing/CZE/ITA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.route[0]").value("CZE"))
                .andExpect(jsonPath("$.route[1]").value("AUT"))
                .andExpect(jsonPath("$.route[2]").value("ITA"));
    }

    @Test
    void shouldReturnBadRequestWhenNoLandRouteExists() throws Exception {
        when(countryRouteService.findRoute("CZE", "USA"))
                .thenThrow(new NoLandRouteException("No land route found from CZE to USA"));

        mockMvc.perform(get("/routing/CZE/USA"))
                .andExpect(status().isBadRequest());
    }
}
