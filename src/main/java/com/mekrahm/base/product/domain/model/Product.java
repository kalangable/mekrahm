package com.mekrahm.base.product.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "product", indexes = {
    @Index(name = "idx_product_ean", columnList = "ean", unique = true),
    @Index(name = "idx_product_uuid", columnList = "resourceId", unique = true)
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "resource_id", nullable = false, unique = true, updatable = false)
    private UUID resourceId = UUID.randomUUID();

    @Column(length = 20, nullable = false, unique = true)
    private String ean;

    @Column(length = 255, nullable = false)
    private String description;

}
