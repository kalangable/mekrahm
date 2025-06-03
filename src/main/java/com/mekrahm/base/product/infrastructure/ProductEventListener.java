package com.mekrahm.base.product.infrastructure;

import com.mekrahm.base.product.domain.event.ProductCreatedEvent;
import com.mekrahm.base.product.domain.event.ProductDeletedEvent;
import com.mekrahm.base.product.domain.event.ProductUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class ProductEventListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProductCreate(ProductCreatedEvent event) {
        log.info("Produto salvo com sucesso: {}", event.getProduct().getEan());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProductUpdated(ProductUpdatedEvent event) {
        log.info("Produto Alterado com sucesso: {}", event.getProduct());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProductDeleted(ProductDeletedEvent event) {
        log.info("Produto deletado com sucesso: {}", event.getProduct());
    }

}
