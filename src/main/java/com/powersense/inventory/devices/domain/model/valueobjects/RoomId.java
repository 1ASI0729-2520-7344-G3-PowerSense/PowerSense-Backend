package com.powersense.inventory.devices.domain.model.valueobjects;

public record RoomId(String value) {
    public RoomId {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("RoomId cannot be null or empty");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
