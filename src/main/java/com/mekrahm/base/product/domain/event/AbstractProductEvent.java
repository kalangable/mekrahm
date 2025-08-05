package com.mekrahm.base.product.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class AbstractProductEvent {

    private final ProductEvent productEvent;
}
