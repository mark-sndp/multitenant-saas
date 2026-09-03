package com.acme.saas.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Resolves the caller's tenant scope from the JWT and exposes it via {@link TenantContext}
 * for the duration of the request, and populates MDC fields for structured request logs.
 */
public class TenantResolutionFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(TenantResolutionFilter.class);
    private static final String TRACE_ID_MDC_KEY = "traceId";
    private static final String TENANT_ID_MDC_KEY = "tenantId";
    private static final String USER_ID_MDC_KEY = "userId";

    private final AcmeSecurityProperties properties;

    public TenantResolutionFilter(AcmeSecurityProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        MDC.put(TRACE_ID_MDC_KEY, UUID.randomUUID().toString());
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                resolveTenant(jwtAuth);
            }
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
            MDC.clear();
        }
    }

    private void resolveTenant(JwtAuthenticationToken jwtAuth) {
        Jwt jwt = jwtAuth.getToken();
        boolean isPlatformAdmin = hasRole(jwtAuth, properties.getAdminRole());
        String tenantId = jwt.getClaimAsString(properties.getTenantClaim());

        TenantContext.set(tenantId, isPlatformAdmin);
        if (tenantId == null && !isPlatformAdmin) {
            log.warn("Authenticated non-platform-admin subject={} has no tenant claim {}",
                jwt.getSubject(), properties.getTenantClaim());
        } else {
            log.debug("Resolved tenant context for subject={}: tenantId={}, bypassRls={}",
                jwt.getSubject(), tenantId, isPlatformAdmin);
        }
        MDC.put(USER_ID_MDC_KEY, jwt.getSubject());
        if (tenantId != null) {
            MDC.put(TENANT_ID_MDC_KEY, tenantId);
        }
    }

    private boolean hasRole(Authentication authentication, String role) {
        String target = "ROLE_" + role;
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (target.equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
