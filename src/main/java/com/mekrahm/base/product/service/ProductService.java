package com.mekrahm.base.product.service;

import com.mekrahm.base.product.ProductDTO;
import com.mekrahm.base.product.mapper.ProductMapper;
import com.mekrahm.base.product.persistence.Product;
import com.mekrahm.base.product.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public ProductDTO findById(Long id) {
        return repository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public ProductDTO save(ProductDTO dto) {
        Product entity = mapper.toEntity(dto);
        entity.setId(null);
        return mapper.toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
