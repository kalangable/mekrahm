package com.mekrahm.base.product.internal;

import com.mekrahm.base.product.ProductPayload;
import com.mekrahm.base.product.domain.model.Product;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ProductUpdaterTest {

    ProductUpdater updater = new ProductUpdater();

    @Test
    void shouldApplyChangesWhenDifferent() {
        Product product = new Product();
        product.setEan("old");
        product.setDescription("old desc");

        ProductPayload payload = new ProductPayload("new", "new desc");

        boolean result = updater.applyChanges(product, payload);

        assertThat(result).isTrue();
        assertThat(product.getEan()).isEqualTo("new");
        assertThat(product.getDescription()).isEqualTo("new desc");
    }

    @Test
    void shouldNotApplyChangesWhenSame() {
        Product product = new Product();
        product.setEan("same");
        product.setDescription("same");

        ProductPayload payload = new ProductPayload("same", "same");

        boolean result = updater.applyChanges(product, payload);

        assertThat(result).isFalse();
    }
}

