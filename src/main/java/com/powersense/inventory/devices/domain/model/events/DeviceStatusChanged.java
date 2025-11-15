package com.powersense.inventory.devices.domain.model.events;

import java.time.Instant;

public record DeviceStatusChanged(
        String deviceId,
        String previousStatus,
        String newStatus,
        Instant occurredOn
) implements DomainEvent {}
