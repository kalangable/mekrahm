package com.mekrahm.base.product.mapper;

import com.mekrahm.base.product.ProductDTO;
import com.mekrahm.base.product.persistence.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {


    @Mapping(source = "ean", target = "ean")
    @Mapping(source = "description", target = "description")
    ProductDTO toDto(Product entity);

    @Mapping(source = "ean", target = "ean")
    @Mapping(source = "description", target = "description")
    Product toEntity(ProductDTO dto);
}
