package com.mekrahm.base.product.web;

import com.mekrahm.base.product.ProductPayload;
import com.mekrahm.base.product.ProductDetails;
import com.mekrahm.base.product.service.ProductCommandService;
import com.mekrahm.base.product.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductQueryService queryService;
    private final ProductCommandService commandService;

    @GetMapping
    public List<ProductDetails> getAll() {
        return queryService.findAll();
    }

    @GetMapping("/{resourceId}")
    public ResponseEntity<ProductDetails> findByResourceId(@PathVariable UUID resourceId) {
        ProductDetails product = queryService.findByResourceId(resourceId);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductDetails> create(@RequestBody ProductPayload product) {
        ProductDetails created = commandService.save(product);
        URI location = URI.create("/products/" + created.getResourceId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{resourceId}")
    public ResponseEntity<ProductDetails> update(@PathVariable UUID resourceId, @RequestBody ProductPayload product) {
        ProductDetails updated = commandService.update(resourceId, product);
        URI location = URI.create("/products/" + updated.getResourceId());
        return ResponseEntity.ok().location(location).body(updated);
    }

    @DeleteMapping("/{resourceId}")
    public ResponseEntity<Void> delete(@PathVariable UUID resourceId) {
        commandService.delete(resourceId);
        return ResponseEntity.noContent().<Void> build();
    }
}
