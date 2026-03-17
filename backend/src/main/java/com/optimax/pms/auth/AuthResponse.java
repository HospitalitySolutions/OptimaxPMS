package com.optimax.pms.auth;

public record AuthResponse(
        String token,
        Long userId,
        String email,
        String role
) {}

