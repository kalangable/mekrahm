package com.mekrahm.base.product.infrastructure;

import com.mekrahm.base.product.domain.event.ProductCreatedEvent;
import com.mekrahm.base.product.domain.event.ProductDeletedEvent;
import com.mekrahm.base.product.domain.event.ProductUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductEventListener {

    private final CacheManager cacheManager;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProductCreate(ProductCreatedEvent event) {
        log.info("Produto salvo com sucesso: {}", event.getProductEvent().resourceId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProductUpdated(ProductUpdatedEvent event) {
        log.info("Produto Alterado com sucesso: {}", event.getProductEvent());
        evictFromCache(event.getProductEvent().resourceId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProductDeleted(ProductDeletedEvent event) {
        log.info("Produto deletado com sucesso: {}", event.getProductEvent());
        evictFromCache(event.getProductEvent().resourceId());
    }

    private void evictFromCache(String resourceId) {
        Cache cache = cacheManager.getCache("productByResourceId");
        if (cache != null) {
            cache.evict(UUID.fromString(resourceId));
            log.info("Produto removido do cache: {}", resourceId);
        }
    }

}
