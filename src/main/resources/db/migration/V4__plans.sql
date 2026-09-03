-- Global reference data (ACME's own subscription tiers), not tenant-scoped, no RLS.
CREATE TABLE plans (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code            VARCHAR(32) NOT NULL UNIQUE,
    name            VARCHAR(255) NOT NULL,
    description     VARCHAR(1024),
    price_cents     BIGINT NOT NULL,
    billing_period  VARCHAR(16) NOT NULL DEFAULT 'MONTHLY',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);
