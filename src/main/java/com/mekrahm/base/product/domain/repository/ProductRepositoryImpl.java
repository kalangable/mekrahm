package com.mekrahm.base.product.domain.repository;

import com.mekrahm.base.product.domain.event.ProductCreatedEvent;
import com.mekrahm.base.product.domain.event.ProductDeletedEvent;
import com.mekrahm.base.product.domain.event.ProductUpdatedEvent;
import com.mekrahm.base.product.domain.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final EntityManager entityManager;
    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public Product saveWithEvent(final Product product) {
        final boolean exists = product.getId() != null && entityManager.find(Product.class, product.getId()) != null;

        final Product saved = entityManager.merge(product);

        if (!exists) {
            publisher.publishEvent(new ProductCreatedEvent(saved));
        } else {
            publisher.publishEvent(new ProductUpdatedEvent(saved));
        }

        return saved;
    }

    @Override
    @Transactional
    public void deleteWithEvent(Product product) {
        entityManager.remove(entityManager.contains(product) ? product : entityManager.merge(product));
        publisher.publishEvent(new ProductDeletedEvent(product));
    }

}
