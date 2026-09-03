package com.acme.saas.product;

import com.acme.saas.security.Roles;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('" + Roles.TENANT_ADMIN + "', '" + Roles.TENANT_USER + "', '" + Roles.PLATFORM_ADMIN + "')")
    public List<ProductResponse> listProducts() {
        return productService.listProducts().stream().map(ProductResponse::from).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('" + Roles.TENANT_ADMIN + "', '" + Roles.TENANT_USER + "', '" + Roles.PLATFORM_ADMIN + "')")
    public ProductResponse getProduct(@PathVariable UUID id) {
        return ProductResponse.from(productService.getProduct(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('" + Roles.TENANT_ADMIN + "', '" + Roles.PLATFORM_ADMIN + "')")
    public ProductResponse createProduct(@Valid @RequestBody CreateProductRequest request) {
        return ProductResponse.from(productService.createProduct(request));
    }
}
