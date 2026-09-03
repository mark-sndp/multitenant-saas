-- Row Level Security: tenant-scoped tables are only visible/writable for the tenant
-- set in the session (app.current_tenant), unless app.bypass_rls is set (platform-admin).
-- FORCE is required because the app connects as acme_app which owns these tables via
-- migrations run by acme_owner but not as superuser; FORCE ensures owners are also subject
-- to policy so a compromised/careless app role change can't silently disable isolation.
-- NULLIF(...,'') avoids a cast error when no tenant is set yet (e.g. outside a request
-- context) — the comparison then simply evaluates to false instead of throwing.

ALTER TABLE users ENABLE ROW LEVEL SECURITY;
ALTER TABLE users FORCE ROW LEVEL SECURITY;
CREATE POLICY tenant_isolation_users ON users
    USING (
        tenant_id = NULLIF(current_setting('app.current_tenant', true), '')::uuid
        OR current_setting('app.bypass_rls', true) = 'true'
    );

ALTER TABLE products ENABLE ROW LEVEL SECURITY;
ALTER TABLE products FORCE ROW LEVEL SECURITY;
CREATE POLICY tenant_isolation_products ON products
    USING (
        tenant_id = NULLIF(current_setting('app.current_tenant', true), '')::uuid
        OR current_setting('app.bypass_rls', true) = 'true'
    );

ALTER TABLE subscriptions ENABLE ROW LEVEL SECURITY;
ALTER TABLE subscriptions FORCE ROW LEVEL SECURITY;
CREATE POLICY tenant_isolation_subscriptions ON subscriptions
    USING (
        tenant_id = NULLIF(current_setting('app.current_tenant', true), '')::uuid
        OR current_setting('app.bypass_rls', true) = 'true'
    );
