package com.example.monolith.user.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank String email,
        @NotBlank String fullName
) {}
