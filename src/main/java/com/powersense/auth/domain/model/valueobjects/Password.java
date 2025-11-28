package com.powersense.auth.domain.model.valueobjects;

public record Password(String value) {

    private static final int MIN_LENGTH = 6;
    private static final int MAX_LENGTH = 100;

    public Password {
        validate(value);
    }

    private void validate(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        if (password.length() < MIN_LENGTH) {
            throw new IllegalArgumentException(
                    "Password must be at least " + MIN_LENGTH + " characters long"
            );
        }

        if (password.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Password cannot exceed " + MAX_LENGTH + " characters"
            );
        }
    }

    public String getValue() {
        return value;
    }
}