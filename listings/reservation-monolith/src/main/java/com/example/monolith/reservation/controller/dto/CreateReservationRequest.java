package com.example.monolith.reservation.controller.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateReservationRequest(
        @NotNull Long userId,
        @NotNull Long resourceId,
        @NotNull @Future LocalDateTime from,
        @NotNull @Future LocalDateTime to
) {}