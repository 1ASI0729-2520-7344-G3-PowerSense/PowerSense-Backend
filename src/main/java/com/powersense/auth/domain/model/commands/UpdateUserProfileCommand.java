package com.powersense.auth.domain.model.commands;

public record UpdateUserProfileCommand(
        Long userId,
        String name,
        String avatarUrl
) {
    public UpdateUserProfileCommand {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Valid user ID is required");
        }
    }
}