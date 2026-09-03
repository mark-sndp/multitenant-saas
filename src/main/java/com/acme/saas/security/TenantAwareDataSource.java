package com.acme.saas.security;

import org.springframework.jdbc.datasource.DelegatingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.lang.reflect.Proxy;

/**
 * Wraps the pooled DataSource so every physical connection checkout carries the
 * current request's tenant into Postgres session variables consumed by RLS policies.
 * Relies on Spring's @Transactional obtaining exactly one connection per transaction,
 * so the SET LOCAL values stay in effect for the whole unit of work.
 */
public class TenantAwareDataSource extends DelegatingDataSource {

    public TenantAwareDataSource(DataSource targetDataSource) {
        super(targetDataSource);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return applyTenant(super.getConnection());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return applyTenant(super.getConnection(username, password));
    }

    /**
     * Applies the current tenant context to the given connection by setting
     * the appropriate Postgres session variables.
     */
    private Connection applyTenant(Connection connection) throws SQLException {
        String tenantId = TenantContext.getTenantId();
        boolean bypassRls = TenantContext.isBypassRls();
        try (Statement statement = connection.createStatement()) {
            statement.execute("SET app.current_tenant = '" + sanitize(tenantId) + "'");
            statement.execute("SET app.bypass_rls = '" + bypassRls + "'");
        }
        return resetOnClose(connection);
    }

    /**
     * Wraps the connection in a proxy that resets the tenant-related session variables
     * when the connection is closed.
     */
    private Connection resetOnClose(Connection connection) {
        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class<?>[]{Connection.class},
                (proxy, method, args) -> {
                    if ("close".equals(method.getName())) {
                        try (Statement statement = connection.createStatement()) {
                            statement.execute("RESET app.current_tenant");
                            statement.execute("RESET app.bypass_rls");
                        } finally {
                            connection.close();
                        }
                        return null;
                    }
                    return method.invoke(connection, args);
                });
    }

    // tenantId is always a UUID string produced/validated server-side (JWT claim or TenantContext),
    // never raw user input, but we still guard against SQL injection defensively.
    private String sanitize(String tenantId) {
        if (tenantId == null) {
            return "";
        }
        if (!tenantId.matches("[0-9a-fA-F-]{0,36}")) {
            throw new IllegalArgumentException("Invalid tenant id format");
        }
        return tenantId;
    }
}
