package com.powersense.inventory.devices.domain.model.valueobjects;

public enum DeviceCategory {
    LIGHT("light"),
    AC("ac"),
    TV("tv"),
    REFRIGERATOR("refrigerator"),
    HEATING("heating"),
    COMPUTER("computer"),
    GENERIC_POWER("genericPower");

    private final String value;

    DeviceCategory(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
