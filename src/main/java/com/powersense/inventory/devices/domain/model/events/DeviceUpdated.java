package com.powersense.inventory.devices.domain.model.events;

import java.time.Instant;

public record DeviceUpdated(
        String deviceId,
        Instant occurredOn
) implements DomainEvent {}
