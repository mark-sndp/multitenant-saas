CREATE TABLE subscriptions (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id               UUID NOT NULL REFERENCES tenants(id),
    plan_id                 UUID NOT NULL REFERENCES plans(id),
    status                  VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
    current_period_start    TIMESTAMPTZ NOT NULL DEFAULT now(),
    current_period_end      TIMESTAMPTZ NOT NULL,
    created_at              TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_subscriptions_tenant_id ON subscriptions (tenant_id);
-- one active subscription per tenant at a time
CREATE UNIQUE INDEX uq_subscriptions_active_tenant ON subscriptions (tenant_id) WHERE status = 'ACTIVE';
