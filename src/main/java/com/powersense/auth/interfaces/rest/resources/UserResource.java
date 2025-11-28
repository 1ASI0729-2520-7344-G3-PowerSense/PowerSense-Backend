package com.powersense.auth.interfaces.rest.resources;

import java.time.LocalDateTime;

public record UserResource(
        Long id,
        String email,
        String name,
        String avatarUrl,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}