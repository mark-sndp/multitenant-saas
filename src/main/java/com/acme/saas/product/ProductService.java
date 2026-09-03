package com.acme.saas.product;

import com.acme.saas.common.NotFoundException;
import com.acme.saas.security.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> listProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product getProduct(UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found: " + id));
    }

    @Transactional
    public Product createProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setTenantId(UUID.fromString(TenantContext.getTenantId()));
        product.setSku(request.sku());
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPriceCents(request.priceCents());
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());
        return productRepository.save(product);
    }
}
