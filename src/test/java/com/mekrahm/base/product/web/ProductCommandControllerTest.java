package com.mekrahm.base.product.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekrahm.base.product.ProductDetails;
import com.mekrahm.base.product.ProductPayload;
import com.mekrahm.base.product.service.ProductCommandService;
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

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductCommandController.class)
@Import(ProductCommandControllerTest.MockedServicesConfig.class)
@ActiveProfiles("test")
class ProductCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
        reset(commandService);
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

    @TestConfiguration
    static class MockedServicesConfig {

        @Bean
        public ProductCommandService productCommandService() {
            return Mockito.mock(ProductCommandService.class);
        }
    }
}

