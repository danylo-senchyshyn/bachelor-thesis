package com.example.monolith.user.controller.dto;

public record UserResponse(
        Long id,
        String email,
        String fullName
) {}
