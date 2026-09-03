package com.acme.saas.plan;

import java.util.UUID;

public record PlanResponse(UUID id, String code, String name, String description, long priceCents, String billingPeriod) {

    public static PlanResponse from(Plan plan) {
        return new PlanResponse(plan.getId(), plan.getCode(), plan.getName(), plan.getDescription(), plan.getPriceCents(), plan.getBillingPeriod());
    }
}
