package com.sohardware.orderservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        String orderNumber,
        String productId,
        BigDecimal price,
        int quantities,
        LocalDateTime createdAt
) {}
