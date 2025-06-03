package com.mekrahm.base.product.domain.event;

import com.mekrahm.base.product.domain.model.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductUpdatedEvent {

    private final Product product;

}
