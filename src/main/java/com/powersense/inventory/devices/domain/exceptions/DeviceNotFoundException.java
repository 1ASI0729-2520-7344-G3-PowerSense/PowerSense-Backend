package com.powersense.inventory.devices.domain.exceptions;

public class DeviceNotFoundException extends DomainException {
    public DeviceNotFoundException(String deviceId) {
        super("Device not found: " + deviceId);
    }
}
