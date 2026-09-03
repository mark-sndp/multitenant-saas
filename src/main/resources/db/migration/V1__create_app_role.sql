-- Dedicated non-superuser application role. RLS policies below only take effect for
-- roles that are NOT the table owner unless FORCE ROW LEVEL SECURITY is used, so the
-- app connects as this restricted role rather than the migration/owner role.
DO
$$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'acme_app') THEN
        CREATE ROLE acme_app LOGIN PASSWORD 'acme_app';
    END IF;
END
$$;

GRANT CONNECT ON DATABASE acme_saas TO acme_app;
