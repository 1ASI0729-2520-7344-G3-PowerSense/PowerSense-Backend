package com.powersense.inventory.devices.domain.model.events;

import java.time.Instant;

public record AllDevicesStatusChanged(
        String newStatus,
        int totalDevices,
        Instant occurredOn
) implements DomainEvent {}
