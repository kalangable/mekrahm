package com.mekrahm.base.product.web;

import com.mekrahm.base.product.ProductDetails;
import com.mekrahm.base.product.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductQueryController {

    private final ProductQueryService queryService;

    @GetMapping
    public List<ProductDetails> getAll() {
        return queryService.findAll();
    }

    @GetMapping("/{resourceId}")
    public ResponseEntity<ProductDetails> findByResourceId(@PathVariable UUID resourceId) {
        ProductDetails product = queryService.findByResourceId(resourceId);
        return ResponseEntity.ok(product);
    }
}
