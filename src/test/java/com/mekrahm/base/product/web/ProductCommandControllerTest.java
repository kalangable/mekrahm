package com.mekrahm.base.product.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekrahm.base.product.ProductPayload;
import com.mekrahm.base.product.ProductDetails;
import com.mekrahm.base.product.service.ProductCommandService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(ProductControllerTest.MockedServicesConfig.class)
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductQueryService queryService;

    @Autowired
    private ProductCommandService commandService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID resourceId;
    private ProductDetails productDetails;
    private ProductPayload createDTO;

    @BeforeEach
    void setup() {
        resourceId = UUID.randomUUID();

        createDTO = new ProductPayload();
        createDTO.setEan("7891234567890");
        createDTO.setDescription("Produto de Teste");

        productDetails = new ProductDetails();
        productDetails.setResourceId(resourceId.toString());
        productDetails.setEan(createDTO.getEan());
        productDetails.setDescription(createDTO.getDescription());

        // Reset mock behavior
        reset(queryService, commandService);
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
    void shouldCreateProduct() throws Exception {
        when(commandService.save(any(ProductPayload.class))).thenReturn(productDetails);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/products/" + resourceId))
            .andExpect(jsonPath("$.resourceId").value(resourceId.toString()));
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        when(commandService.update(eq(resourceId), any(ProductPayload.class))).thenReturn(productDetails);

        mockMvc.perform(put("/products/{id}", resourceId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resourceId").value(resourceId.toString()));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        doNothing().when(commandService).delete(resourceId);

        mockMvc.perform(delete("/products/{id}", resourceId))
            .andExpect(status().isNoContent());
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

        @Bean
        public ProductCommandService productCommandService() {
            return Mockito.mock(ProductCommandService.class);
        }
    }
}

