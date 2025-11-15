package com.powersense.inventory.devices.domain.model.events;

import java.time.Instant;

public record DevicesImported(
        int totalImported,
        Instant occurredOn
) implements DomainEvent {}
