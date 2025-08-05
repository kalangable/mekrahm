package com.mekrahm.base.product.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekrahm.base.product.ProductDetails;
import com.mekrahm.base.product.ProductPayload;
import com.mekrahm.base.product.service.ProductQueryService;
import com.mekrahm.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductQueryController.class)
@Import(ProductQueryControllerTest.MockedServicesConfig.class)
@ActiveProfiles("test")
class ProductQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductQueryService queryService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID resourceId;
    private ProductDetails productDetails;
    private ProductPayload productPayload;

    @BeforeEach
    void setup() {
        resourceId = UUID.randomUUID();

        productPayload = new ProductPayload();
        productPayload.setEan("7891234567890");
        productPayload.setDescription("Produto de Teste");

        productDetails = new ProductDetails();
        productDetails.setResourceId(resourceId.toString());
        productDetails.setEan(productPayload.getEan());
        productDetails.setDescription(productPayload.getDescription());

        // Reset mock behavior
        reset(queryService);
    }

    @Test
    void shouldReturnAllProducts() throws Exception {
        when(queryService.findAll()).thenReturn(List.of(productDetails));

        mockMvc.perform(get("/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].ean").value("7891234567890"));
    }

    @Test
    void shouldReturnProductById() throws Exception {
        when(queryService.findByResourceId(resourceId)).thenReturn(productDetails);

        mockMvc.perform(get("/products/{id}", resourceId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.description").value("Produto de Teste"));
    }

    @Test
    void shouldReturn404WhenProductNotFound() throws Exception {
        UUID notFoundId = UUID.randomUUID();

        when(queryService.findByResourceId(notFoundId))
            .thenThrow(new NotFoundException("Product not found"));

        mockMvc.perform(get("/products/{id}", notFoundId.toString()))
            .andExpect(status().isNotFound());
    }

    @TestConfiguration
    static class MockedServicesConfig {

        @Bean
        public ProductQueryService productQueryService() {
            return Mockito.mock(ProductQueryService.class);
        }

    }
}
