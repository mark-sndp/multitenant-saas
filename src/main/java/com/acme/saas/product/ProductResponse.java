package com.acme.saas.product;

import java.time.Instant;
import java.util.UUID;

public record ProductResponse(UUID id, UUID tenantId, String sku, String name, String description,
                               long priceCents, Instant createdAt) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getTenantId(), product.getSku(), product.getName(),
                product.getDescription(), product.getPriceCents(), product.getCreatedAt());
    }
}
