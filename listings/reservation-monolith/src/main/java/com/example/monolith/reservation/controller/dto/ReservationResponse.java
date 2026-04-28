package com.example.monolith.reservation.controller.dto;

import com.example.monolith.reservation.entity.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Long userId,
        Long resourceId,
        LocalDateTime from,
        LocalDateTime to,
        ReservationStatus status
) {}