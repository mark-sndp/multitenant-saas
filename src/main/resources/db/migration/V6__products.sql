-- Each tenant's own catalog data; the primary example used to demonstrate RLS isolation.
CREATE TABLE products (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES tenants(id),
    sku             VARCHAR(64) NOT NULL,
    name            VARCHAR(255) NOT NULL,
    description     VARCHAR(1024),
    price_cents     BIGINT NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (tenant_id, sku)
);

CREATE INDEX idx_products_tenant_id ON products (tenant_id);
