package com.acme.saas.subscription;

import com.acme.saas.common.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** RLS on the subscriptions table means the current tenant's
 *  active subscription is all that is visible. */
@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional(readOnly = true)
    public Subscription getCurrentSubscription() {
        return subscriptionRepository.findFirstByStatusOrderByCreatedAtDesc(SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new NotFoundException("No active subscription for current tenant"));
    }
}
