package com.mekrahm.base.product;

import com.mekrahm.base.product.domain.model.Product;
import com.mekrahm.base.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository repository;

    public Optional<Product> findById(final long id) {
        return repository.findById(id);
    }

}
