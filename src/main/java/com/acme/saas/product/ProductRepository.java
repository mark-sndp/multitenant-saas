package com.acme.saas.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/** No manual tenant_id filtering needed here — Postgres RLS restricts every query transparently. */
public interface ProductRepository extends JpaRepository<Product, UUID> {
}
