package com.mekrahm.base.product.internal;

import com.mekrahm.base.product.ProductDetails;
import com.mekrahm.base.product.ProductPayload;
import com.mekrahm.base.product.domain.event.ProductEvent;
import com.mekrahm.base.product.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDetails toDto(Product product);

    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductDetails productDetails);

    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductPayload productPayload);

    ProductEvent toEvent(Product product);
}
