package com.mekrahm.base.product.service;

import com.mekrahm.base.product.ProductCreateDTO;
import com.mekrahm.base.product.ProductDTO;
import com.mekrahm.base.product.mapper.ProductMapper;
import com.mekrahm.base.product.persistence.Product;
import com.mekrahm.base.product.persistence.ProductRepository;
import com.mekrahm.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public List<ProductDTO> findAll() {
        return repository.findAll().stream()
            .map(mapper::toDto)
            .toList();
    }

    public ProductDTO findByResourceId(final String resourceId) {
        final UUID uuid = UUID.fromString(resourceId);
        return repository.findByResourceId(uuid)
            .map(mapper::toDto)
            .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    public ProductDTO save(ProductCreateDTO productCreateDTO) {
        Product entity = mapper.toEntity(productCreateDTO);
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
