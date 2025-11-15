package com.powersense.inventory.scheduling.domain.exceptions;

public class DuplicateScheduleException extends DomainException {
    public DuplicateScheduleException(String deviceId) {
        super("Device already has a schedule: " + deviceId);
    }
}
