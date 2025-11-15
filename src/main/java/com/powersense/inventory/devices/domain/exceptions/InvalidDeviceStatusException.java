package com.powersense.inventory.devices.domain.exceptions;

public class InvalidDeviceStatusException extends DomainException {
    public InvalidDeviceStatusException(String message) {
        super(message);
    }
}
