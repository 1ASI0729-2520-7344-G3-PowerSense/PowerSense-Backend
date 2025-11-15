package com.powersense.inventory.devices.domain.model.valueobjects;

import java.util.Objects;

public record DeviceId(String value) {
    public DeviceId {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("DeviceId cannot be null or empty");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
