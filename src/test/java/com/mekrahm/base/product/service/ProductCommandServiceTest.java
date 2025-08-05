package com.mekrahm.base.product.service;

import com.mekrahm.base.product.ProductDetails;
import com.mekrahm.base.product.ProductPayload;
import com.mekrahm.base.product.domain.model.Product;
import com.mekrahm.base.product.domain.repository.ProductRepository;
import com.mekrahm.base.product.internal.ProductMapper;
import com.mekrahm.base.product.internal.ProductUpdater;
import com.mekrahm.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductCommandServiceTest {

    private final UUID resourceId = UUID.randomUUID();
    @Mock
    private ProductRepository repository;
    @Mock
    private ProductMapper mapper;
    @Mock
    private ProductUpdater updater;
    @InjectMocks
    private ProductCommandService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_ShouldPersistAndReturnDto() {
        ProductPayload payload = new ProductPayload("123456789", "Test Product");
        Product entity = new Product();
        Product saved = new Product();
        ProductDetails dto = new ProductDetails();

        when(mapper.toEntity(payload)).thenReturn(entity);
        when(repository.persist(entity)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(dto);

        ProductDetails result = service.save(payload);

        assertSame(dto, result);
        verify(mapper).toEntity(payload);
        verify(repository).persist(entity);
        verify(mapper).toDto(saved);
    }

    @Test
    void update_ShouldPersistWhenChangesExist() {
        ProductPayload payload = new ProductPayload("ean-new", "desc-new");
        Product product = new Product();
        ProductDetails dto = new ProductDetails();

        when(repository.findByResourceId(resourceId)).thenReturn(Optional.of(product));
        when(updater.applyChanges(product, payload)).thenReturn(true);
        when(repository.persist(product)).thenReturn(product);
        when(mapper.toDto(product)).thenReturn(dto);

        ProductDetails result = service.update(resourceId, payload);

        assertSame(dto, result);
        verify(repository).persist(product);
    }

    @Test
    void update_ShouldNotPersistWhenNoChanges() {
        ProductPayload payload = new ProductPayload("ean", "desc");
        Product product = new Product();
        ProductDetails dto = new ProductDetails();

        when(repository.findByResourceId(resourceId)).thenReturn(Optional.of(product));
        when(updater.applyChanges(product, payload)).thenReturn(false);
        when(mapper.toDto(product)).thenReturn(dto);

        ProductDetails result = service.update(resourceId, payload);

        assertSame(dto, result);
        verify(repository, never()).persist(any());
    }

    @Test
    void delete_ShouldPurgeProduct() {
        Product product = new Product();

        when(repository.findByResourceId(resourceId)).thenReturn(Optional.of(product));

        service.delete(resourceId);

        verify(repository).purge(product);
    }

    @Test
    void getProductOrThrow_ShouldThrowWhenNotFound() {
        when(repository.findByResourceId(resourceId)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(
            NotFoundException.class,
            () -> service.delete(resourceId)
        );

        assertEquals("Product not found to delete", thrown.getMessage());
    }
}
