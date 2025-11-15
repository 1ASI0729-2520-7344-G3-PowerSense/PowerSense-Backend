package com.powersense.inventory.devices.domain.model.events;

import java.time.Instant;

public record RoomDevicesStatusChanged(
        String roomId,
        String roomName,
        String newStatus,
        int affectedDevices,
        Instant occurredOn
) implements DomainEvent {}
