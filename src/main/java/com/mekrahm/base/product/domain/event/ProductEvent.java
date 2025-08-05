package com.mekrahm.base.product.domain.event;

public record ProductEvent(String resourceId, String ean, String description) {

}
