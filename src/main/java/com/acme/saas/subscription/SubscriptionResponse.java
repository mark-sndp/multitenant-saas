package com.acme.saas.subscription;

import java.time.Instant;
import java.util.UUID;

public record SubscriptionResponse(UUID id, UUID tenantId, UUID planId, SubscriptionStatus status,
                                    Instant currentPeriodStart, Instant currentPeriodEnd) {

    public static SubscriptionResponse from(Subscription subscription) {
        return new SubscriptionResponse(subscription.getId(), subscription.getTenantId(), subscription.getPlanId(),
                subscription.getStatus(), subscription.getCurrentPeriodStart(), subscription.getCurrentPeriodEnd());
    }
}
