package com.mekrahm.base.product.domain.event;

public class ProductUpdatedEvent extends AbstractProductEvent {

    public ProductUpdatedEvent(ProductEvent productEvent) {
        super(productEvent);
    }
}
