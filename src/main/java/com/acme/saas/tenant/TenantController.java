package com.acme.saas.tenant;

import com.acme.saas.security.Roles;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/tenants")
@PreAuthorize("hasRole('" + Roles.PLATFORM_ADMIN + "')")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping
    public List<TenantResponse> listTenants() {
        return tenantService.listTenants().stream().map(TenantResponse::from).toList();
    }

    @GetMapping("/{id}")
    public TenantResponse getTenant(@PathVariable UUID id) {
        return TenantResponse.from(tenantService.getTenant(id));
    }
}
