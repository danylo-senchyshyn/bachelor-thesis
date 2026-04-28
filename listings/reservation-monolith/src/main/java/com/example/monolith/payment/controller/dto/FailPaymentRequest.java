package com.example.monolith.payment.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record FailPaymentRequest(
        @NotBlank String reason
) {
}
