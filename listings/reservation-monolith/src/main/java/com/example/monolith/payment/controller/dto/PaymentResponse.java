package com.example.monolith.payment.controller.dto;

import com.example.monolith.payment.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long reservationId,
        BigDecimal amount,
        PaymentStatus status,
        LocalDateTime createdAt
) {
}
