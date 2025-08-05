package com.mekrahm.base.product.service;

import com.mekrahm.base.product.ProductDetails;
import com.mekrahm.base.product.domain.repository.ProductRepository;
import com.mekrahm.base.product.internal.ProductMapper;
import com.mekrahm.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductQueryService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public List<ProductDetails> findAll() {
        return repository.findAll().stream()
            .map(mapper::toDto)
            .toList();
    }

    @Cacheable(value = "productByResourceId", key = "#resourceId")
    public ProductDetails findByResourceId(final UUID resourceId) {
        return repository.findByResourceId(resourceId)
            .map(mapper::toDto)
            .orElseThrow(() -> new NotFoundException("Product not found"));
    }
}
