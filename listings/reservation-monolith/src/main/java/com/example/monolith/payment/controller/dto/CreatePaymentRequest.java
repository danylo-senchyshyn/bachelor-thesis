package com.example.monolith.payment.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreatePaymentRequest(
        @NotNull Long reservationId,
        @NotNull @Positive BigDecimal amount
) {
}
