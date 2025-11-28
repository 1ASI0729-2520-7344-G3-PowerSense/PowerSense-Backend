package com.powersense.auth.domain.model.commands;

public record LoginUserCommand(
        String email,
        String password
) {
    public LoginUserCommand {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
    }
}