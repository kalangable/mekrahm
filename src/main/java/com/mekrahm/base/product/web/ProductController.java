package com.mekrahm.base.product.web;

import com.mekrahm.base.product.ProductCreateDTO;
import com.mekrahm.base.product.ProductDTO;
import com.mekrahm.base.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    private final ProductService service;

    @GetMapping
    public List<ProductDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{resourceId}")
    public ResponseEntity<ProductDTO> findByEan(@PathVariable String resourceId) {
        ProductDTO product = service.findByResourceId(resourceId);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductCreateDTO product) {
        ProductDTO created = service.save(product);
        URI location = URI.create("/products/" + created.getResourceId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{resourceId}")
    public ResponseEntity<ProductDTO> update(@PathVariable String resourceId, @RequestBody ProductCreateDTO product) {
        ProductDTO updated = service.update(resourceId, product);
        URI location = URI.create("/products/" + updated.getResourceId());
        return ResponseEntity.ok().location(location).body(updated);
    }

    /*
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String resourceId) {
        return service.findById(id)
            .map(p -> {
                service.delete(id);
                return ResponseEntity.noContent().<Void> build();
            })
            .orElse(ResponseEntity.notFound().build());
    }*/
}
