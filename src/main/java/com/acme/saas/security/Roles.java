package com.acme.saas.security;

/** Realm role names, matching keycloak/realm-export.json — kept as literals for @PreAuthorize SpEL. */
public final class Roles {

    public static final String PLATFORM_ADMIN = "platform-admin";
    public static final String TENANT_ADMIN = "tenant-admin";
    public static final String TENANT_USER = "tenant-user";

    private Roles() {
    }
}
