package com.powersense.inventory.devices.domain.model.valueobjects;

public record Location(RoomId roomId, RoomName roomName) {
    public Location {
        if (roomId == null) {
            throw new IllegalArgumentException("Location.roomId cannot be null");
        }
        if (roomName == null) {
            throw new IllegalArgumentException("Location.roomName cannot be null");
        }
    }
}
