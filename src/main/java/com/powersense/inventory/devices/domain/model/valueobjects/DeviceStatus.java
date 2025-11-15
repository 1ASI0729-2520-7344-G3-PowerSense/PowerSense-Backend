package com.powersense.inventory.devices.domain.model.valueobjects;

public enum DeviceStatus {
    ACTIVE("active"),
    INACTIVE("inactive");

    private final String value;

    DeviceStatus(String value) {
        this.value = value;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isInactive() {
        return this == INACTIVE;
    }

    public DeviceStatus toggle() {
        return this == ACTIVE ? INACTIVE : ACTIVE;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
