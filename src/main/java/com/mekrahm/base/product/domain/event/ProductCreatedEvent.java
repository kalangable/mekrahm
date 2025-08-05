package com.mekrahm.base.product.domain.event;

public class ProductCreatedEvent extends AbstractProductEvent {

    public ProductCreatedEvent(ProductEvent productEvent) {
        super(productEvent);
    }
}
