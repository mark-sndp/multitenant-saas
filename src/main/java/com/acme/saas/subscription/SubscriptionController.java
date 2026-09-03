package com.acme.saas.subscription;

import com.acme.saas.security.Roles;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/current")
    @PreAuthorize("hasAnyRole('" + Roles.TENANT_ADMIN + "', '" + Roles.TENANT_USER + "', '" + Roles.PLATFORM_ADMIN + "')")
    public SubscriptionResponse getCurrentSubscription() {
        return SubscriptionResponse.from(subscriptionService.getCurrentSubscription());
    }
}
