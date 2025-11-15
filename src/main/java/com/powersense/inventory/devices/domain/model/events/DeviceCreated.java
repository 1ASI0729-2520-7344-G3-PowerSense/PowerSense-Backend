package com.powersense.inventory.devices.domain.model.events;

import java.time.Instant;

public record DeviceCreated(
        String deviceId,
        String deviceName,
        Instant occurredOn
) implements DomainEvent {}
