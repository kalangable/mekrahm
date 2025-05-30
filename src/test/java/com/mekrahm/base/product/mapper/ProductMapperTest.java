package com.mekrahm.base.product.mapper;

import com.mekrahm.base.product.persistence.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductMapperTest {

    @Autowired
    ProductMapper mapper;

    @Test
    void testMapping() {
        var entity = new Product();
        entity.setEan("123456789");
        entity.setDescription("Example Product");

        var dto = mapper.toDto(entity);

        Assertions.assertEquals("123456789", dto.getEan());
        Assertions.assertEquals("Example Product", dto.getDescription());
    }
}
