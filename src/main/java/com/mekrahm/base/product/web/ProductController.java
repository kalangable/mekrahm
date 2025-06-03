package com.mekrahm.base.product.web;

import com.mekrahm.base.product.ProductCreateDTO;
import com.mekrahm.base.product.ProductDTO;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductQueryService queryService;
    private final ProductCommandService commnadService;

    @GetMapping
    public List<ProductDTO> getAll() {
        return queryService.findAll();
    }

    @GetMapping("/{resourceId}")
    public ResponseEntity<ProductDTO> findByEan(@PathVariable String resourceId) {
        ProductDTO product = queryService.findByResourceId(resourceId);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductCreateDTO product) {
        ProductDTO created = commnadService.save(product);
        URI location = URI.create("/products/" + created.getResourceId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{resourceId}")
    public ResponseEntity<ProductDTO> update(@PathVariable String resourceId, @RequestBody ProductCreateDTO product) {
        ProductDTO updated = commnadService.update(resourceId, product);
        URI location = URI.create("/products/" + updated.getResourceId());
        return ResponseEntity.ok().location(location).body(updated);
    }

    @DeleteMapping("/{resourceId}")
    public ResponseEntity<Void> delete(@PathVariable String resourceId) {
        commnadService.delete(resourceId);
        return ResponseEntity.noContent().<Void> build();
    }
}
