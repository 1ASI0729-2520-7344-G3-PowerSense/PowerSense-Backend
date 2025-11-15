package com.powersense.inventory.devices.domain.model.valueobjects;

public record DeviceName(String value) {
    private static final int MAX_LENGTH = 100;

    public DeviceName {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("DeviceName cannot be null or empty");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("DeviceName cannot exceed " + MAX_LENGTH + " characters");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
