package com.mekrahm.base.product.domain.repository;

import com.mekrahm.base.product.domain.model.Product;
import com.mekrahm.base.product.infrastructure.event.ProductEventDispatcher;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final EntityManager entityManager;
    private final ProductEventDispatcher productEventDispatcher;

    private final JpaProductRepository jpa;

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findByResourceId(UUID resourceId) {
        return jpa.findByResourceId(resourceId);
    }

    @Override
    public List<Product> findAll() {
        return jpa.findAll();
    }

    @Override
    @Transactional
    public Product persist(Product product) {
        boolean exists = exists(product);
        Product saved = entityManager.merge(product);
        productEventDispatcher.publishCreateOrUpdate(saved, exists);
        return saved;
    }

    private boolean exists(Product product) {
        return product.getId() != null && entityManager.find(Product.class, product.getId()) != null;
    }

    @Override
    @Transactional
    public void purge(Product product) {
        Product managed = entityManager.contains(product) ? product : entityManager.merge(product);
        entityManager.remove(managed);
        productEventDispatcher.publishDelete(product);
    }
}
