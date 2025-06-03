package com.mekrahm.base.product.service;

import com.mekrahm.base.product.ProductCreateDTO;
import com.mekrahm.base.product.ProductDTO;
import com.mekrahm.base.product.domain.model.Product;
import com.mekrahm.base.product.domain.repository.ProductRepository;
import com.mekrahm.base.product.internal.ProductMapper;
import com.mekrahm.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCommandService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductDTO save(ProductCreateDTO productCreateDTO) {
        Product entity = mapper.toEntity(productCreateDTO);
        return mapper.toDto(repository.saveWithEvent(entity));
    }

    public ProductDTO update(final UUID resourceId, final ProductCreateDTO dto) {
        final Product product = loadProductOrThrow(resourceId, "Product not found to update");

        boolean hasChanges = applyChanges(product, dto);

        Product savedProduct = hasChanges ? repository.saveWithEvent(product) : product;

        if (!hasChanges) {
            log.info("Same object, not updated");
        }

        return mapper.toDto(savedProduct);
    }

    public void delete(final UUID resourceId) {
        Product product = loadProductOrThrow(resourceId, "Product not found to delete");
        repository.deleteWithEvent(product);
    }

    private Product loadProductOrThrow(final UUID resourceId, String message) {
        return repository.findByResourceId(resourceId)
            .orElseThrow(() -> new NotFoundException(message));
    }

    private boolean applyChanges(Product product, ProductCreateDTO dto) {
        boolean updated = false;

        if (!Objects.equals(product.getEan(), dto.getEan())) {
            product.setEan(dto.getEan());
            updated = true;
        }

        if (!Objects.equals(product.getDescription(), dto.getDescription())) {
            product.setDescription(dto.getDescription());
            updated = true;
        }

        return updated;
    }

}
