package com.mekrahm.base.product.domain.repository;

import com.mekrahm.base.product.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByResourceId(final UUID resourceId);

}
