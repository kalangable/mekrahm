package com.mekrahm.base.product.domain.repository;

import com.mekrahm.base.product.domain.model.Product;

public interface ProductRepositoryCustom {

    Product saveWithEvent(Product product);

    void deleteWithEvent(Product product);

}
