package com.powersense.inventory.scheduling.domain.model.events;

import java.time.Instant;

public record QuickScheduleApplied(
		String presetId,
		String presetName,
		int devicesAffected,
		Instant occurredOn
) implements DomainEvent {}
