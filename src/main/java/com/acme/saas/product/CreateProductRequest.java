package com.acme.saas.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateProductRequest(
        @NotBlank String sku,
        @NotBlank String name,
        String description,
        @PositiveOrZero long priceCents
) {
}
