package com.mekrahm.base.product.infrastructure.event;

import com.mekrahm.base.product.domain.event.ProductCreatedEvent;
import com.mekrahm.base.product.domain.event.ProductDeletedEvent;
import com.mekrahm.base.product.domain.event.ProductEvent;
import com.mekrahm.base.product.domain.event.ProductUpdatedEvent;
import com.mekrahm.base.product.domain.model.Product;
import com.mekrahm.base.product.internal.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEventDispatcher {

    private final ApplicationEventPublisher publisher;
    private final ProductMapper mapper;

    public void publishCreateOrUpdate(Product product, boolean existedBefore) {
        ProductEvent event = mapper.toEvent(product);
        if (existedBefore) {
            publisher.publishEvent(new ProductUpdatedEvent(event));
        } else {
            publisher.publishEvent(new ProductCreatedEvent(event));
        }
    }

    public void publishDelete(Product product) {
        ProductEvent event = mapper.toEvent(product);
        publisher.publishEvent(new ProductDeletedEvent(event));
    }
}
