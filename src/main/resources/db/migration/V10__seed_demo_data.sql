-- Demo tenants used by the sample Keycloak users (see keycloak/realm-export.json).
INSERT INTO tenants (id, slug, name, status) VALUES
    ('11111111-1111-1111-1111-111111111111', 'northwind-traders', 'Northwind Traders', 'ACTIVE'),
    ('22222222-2222-2222-2222-222222222222', 'globex-industries', 'Globex Industries', 'ACTIVE');

INSERT INTO subscriptions (tenant_id, plan_id, status, current_period_start, current_period_end)
SELECT '11111111-1111-1111-1111-111111111111', id, 'ACTIVE', now(), now() + interval '30 days'
FROM plans WHERE code = 'PROFESSIONAL';

INSERT INTO subscriptions (tenant_id, plan_id, status, current_period_start, current_period_end)
SELECT '22222222-2222-2222-2222-222222222222', id, 'ACTIVE', now(), now() + interval '30 days'
FROM plans WHERE code = 'STARTER';

INSERT INTO products (tenant_id, sku, name, description, price_cents) VALUES
    ('11111111-1111-1111-1111-111111111111', 'NW-WIDGET-1', 'Widget', 'Standard widget', 1999),
    ('11111111-1111-1111-1111-111111111111', 'NW-GADGET-1', 'Gadget', 'Deluxe gadget', 4999),
    ('22222222-2222-2222-2222-222222222222', 'GX-GIZMO-1', 'Gizmo', 'Globex flagship gizmo', 2999);
