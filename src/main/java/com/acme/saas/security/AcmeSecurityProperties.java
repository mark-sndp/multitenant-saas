package com.acme.saas.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "acme.keycloak")
public class AcmeSecurityProperties {

    private String adminRole = "platform-admin";
    private String tenantAdminRole = "tenant-admin";
    private String tenantUserRole = "tenant-user";
    private String tenantClaim = "tenant_id";

    public String getAdminRole() {
        return adminRole;
    }

    public void setAdminRole(String adminRole) {
        this.adminRole = adminRole;
    }

    public String getTenantAdminRole() {
        return tenantAdminRole;
    }

    public void setTenantAdminRole(String tenantAdminRole) {
        this.tenantAdminRole = tenantAdminRole;
    }

    public String getTenantUserRole() {
        return tenantUserRole;
    }

    public void setTenantUserRole(String tenantUserRole) {
        this.tenantUserRole = tenantUserRole;
    }

    public String getTenantClaim() {
        return tenantClaim;
    }

    public void setTenantClaim(String tenantClaim) {
        this.tenantClaim = tenantClaim;
    }
}
