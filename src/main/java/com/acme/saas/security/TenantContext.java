package com.acme.saas.security;

/**
 * Per-request holder for the authenticated caller's tenant scope, read by
 * {@link TenantAwareDataSource} when a physical connection is checked out.
 */
public final class TenantContext {

    private static final ThreadLocal<String> TENANT_ID = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> BYPASS_RLS = new ThreadLocal<>();

    private TenantContext() {
    }

    public static void set(String tenantId, boolean bypassRls) {
        TENANT_ID.set(tenantId);
        BYPASS_RLS.set(bypassRls);
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
    }
}
