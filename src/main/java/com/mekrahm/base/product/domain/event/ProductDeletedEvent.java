package com.mekrahm.base.product.domain.event;

public class ProductDeletedEvent extends AbstractProductEvent {

    public ProductDeletedEvent(ProductEvent productEvent) {
        super(productEvent);
    }
}
