package com.mekrahm.base.product.service;

import com.mekrahm.base.product.ProductCreateDTO;
import com.mekrahm.base.product.ProductDTO;
import com.mekrahm.base.product.mapper.ProductMapper;
import com.mekrahm.base.product.persistence.Product;
import com.mekrahm.base.product.persistence.ProductRepository;
import com.mekrahm.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public List<ProductDTO> findAll() {
        return repository.findAll().stream()
            .map(mapper::toDto)
            .toList();
    }

    public ProductDTO findByResourceId(final String resourceId) {
        final Product product = findOrThrow(resourceId, "Product not found");
        return mapper.toDto(product);
    }

    public ProductDTO save(ProductCreateDTO productCreateDTO) {
        Product entity = mapper.toEntity(productCreateDTO);
        return mapper.toDto(repository.save(entity));
    }

    public ProductDTO update(final String resourceId, final ProductCreateDTO productCreateDTO) {
        final Product product = findOrThrow(resourceId, "Product not found to update");

        boolean hasChanges = false;

        if (!Objects.equals(product.getEan(), productCreateDTO.getEan())) {
            product.setEan(productCreateDTO.getEan());
            hasChanges = true;
        }

        if (!Objects.equals(product.getDescription(), productCreateDTO.getDescription())) {
            product.setDescription(productCreateDTO.getDescription());
            hasChanges = true;
        }

        Product savedProduct = null;
        if (hasChanges) {
            savedProduct = repository.save(product);
        } else {
            savedProduct = product;
            log.info("Same object, not updated");
        }
        return mapper.toDto(savedProduct);
    }

    public void delete(final String resourceId) {

        final Product product = findOrThrow(resourceId, "Product not found to delete");

        repository.deleteById(product.getId());
    }

    private Product findOrThrow(final String resourceId, String message) {
        final UUID uuid = UUID.fromString(resourceId);
        final Product product = repository.findByResourceId(uuid)
            .orElseThrow(() -> new NotFoundException(message));
        return product;
    }
}
