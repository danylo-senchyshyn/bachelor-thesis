package com.example.monolith.payment.controller.dto;

public record PaymentFailedRequest(
        Long paymentId,
        Long reservationId,
        String reason
) {}