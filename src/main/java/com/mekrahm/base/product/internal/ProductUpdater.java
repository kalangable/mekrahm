package com.mekrahm.base.product.internal;

import com.mekrahm.base.product.ProductPayload;
import com.mekrahm.base.product.domain.model.Product;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ProductUpdater {

    public boolean applyChanges(Product product, ProductPayload payload) {
        boolean updated = false;

        if (!Objects.equals(product.getEan(), payload.getEan())) {
            product.setEan(payload.getEan());
            updated = true;
        }

        if (!Objects.equals(product.getDescription(), payload.getDescription())) {
            product.setDescription(payload.getDescription());
            updated = true;
        }

        return updated;
    }
}
