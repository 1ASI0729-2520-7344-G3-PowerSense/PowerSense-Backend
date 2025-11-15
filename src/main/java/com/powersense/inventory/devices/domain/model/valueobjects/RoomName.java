package com.powersense.inventory.devices.domain.model.valueobjects;

public record RoomName(String value) {
    private static final int MAX_LENGTH = 50;

    public RoomName {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("RoomName cannot be null or empty");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("RoomName cannot exceed " + MAX_LENGTH + " characters");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
