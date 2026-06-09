package com.sohardware.orderservice.dto;

import java.math.BigDecimal;

public record OrderRequest(
        String productId,
        BigDecimal  price,
        int quantity
) {
}
