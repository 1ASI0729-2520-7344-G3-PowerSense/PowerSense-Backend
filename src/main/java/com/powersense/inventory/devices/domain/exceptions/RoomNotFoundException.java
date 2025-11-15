package com.powersense.inventory.devices.domain.exceptions;

public class RoomNotFoundException extends DomainException {
    public RoomNotFoundException(String roomId) {
        super("Room not found: " + roomId);
    }
}
