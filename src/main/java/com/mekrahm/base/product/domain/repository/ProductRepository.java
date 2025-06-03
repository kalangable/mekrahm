package com.mekrahm.base.product.domain.repository;

import com.mekrahm.base.product.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    Optional<Product> findByResourceId(final UUID resourceId);

    @Override
    @Deprecated(since = "Use saveWithEvent() via ProductService")
    default Product save(Product entity) {
        throw new UnsupportedOperationException("Use ProductService.saveWithEvent()");
    }

    @Override
    @Deprecated(since = "Use deleteWithEvent() via ProductService")
    default void delete(Product entity) {
        throw new UnsupportedOperationException("Use ProductService.deleteWithEvent()");
    }

}
