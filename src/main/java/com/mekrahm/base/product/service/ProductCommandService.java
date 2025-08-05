package com.mekrahm.base.product.service;

import com.mekrahm.base.product.ProductDetails;
import com.mekrahm.base.product.ProductPayload;
import com.mekrahm.base.product.domain.model.Product;
import com.mekrahm.base.product.domain.repository.ProductRepository;
import com.mekrahm.base.product.internal.ProductMapper;
import com.mekrahm.base.product.internal.ProductUpdater;
import com.mekrahm.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCommandService {

    private final ProductRepository repository;

    private final ProductMapper mapper;

    private final ProductUpdater updater;

    public ProductDetails save(ProductPayload productPayload) {
        Product entity = mapper.toEntity(productPayload);
        return mapper.toDto(repository.persist(entity));
    }

    public ProductDetails update(UUID resourceId, ProductPayload productPayload) {
        Product product = getProductOrThrow(resourceId, "Product not found to update");

        boolean hasChanges = updater.applyChanges(product, productPayload);

        Product savedProduct = hasChanges ? repository.persist(product) : product;

        if (!hasChanges) {
            log.debug("No changes detected for product with resourceId={}", resourceId);
        }

        return mapper.toDto(savedProduct);
    }

    public void delete(final UUID resourceId) {
        Product product = getProductOrThrow(resourceId, "Product not found to delete");
        repository.purge(product);
    }

    private Product getProductOrThrow(final UUID resourceId, String message) {
        return repository.findByResourceId(resourceId)
            .orElseThrow(() -> new NotFoundException(message));
    }

}
