package com.powersense.inventory.devices.domain.model.events;

import java.time.Instant;

public record DeviceDeleted(
        String deviceId,
        Instant occurredOn
) implements DomainEvent {}
