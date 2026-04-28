package com.example.monolith.payment.controller.dto;

public record PaymentConfirmedRequest(
        Long paymentId,
        Long reservationId
) {}