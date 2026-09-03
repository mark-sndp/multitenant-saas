package com.acme.saas.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Per-request holder for the authenticated caller's tenant scope, read by
 * {@link TenantAwareDataSource} when a physical connection is checked out.
 */
public final class TenantContext {

    private static final Logger log = LoggerFactory.getLogger(TenantContext.class);
    private static final ThreadLocal<String> TENANT_ID = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> BYPASS_RLS = new ThreadLocal<>();

    private TenantContext() {
    }

    public static void set(String tenantId, boolean bypassRls) {
        TENANT_ID.set(tenantId);
        BYPASS_RLS.set(bypassRls);
        log.debug("Set tenant context: tenantId={}, bypassRls={}", tenantId, bypassRls);
    }

    public static String getTenantId() {
        return TENANT_ID.get();
    }

    public static boolean isBypassRls() {
        return Boolean.TRUE.equals(BYPASS_RLS.get());
    }

    public static void clear() {
        TENANT_ID.remove();
        BYPASS_RLS.remove();
        log.debug("Cleared tenant context");
    }
}
