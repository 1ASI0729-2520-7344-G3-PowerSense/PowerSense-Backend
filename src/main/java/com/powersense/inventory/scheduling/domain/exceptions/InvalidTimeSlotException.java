package com.powersense.inventory.scheduling.domain.exceptions;

public class InvalidTimeSlotException extends DomainException {
    public InvalidTimeSlotException(String message) {
        super("Invalid time slot: " + message);
    }
}
