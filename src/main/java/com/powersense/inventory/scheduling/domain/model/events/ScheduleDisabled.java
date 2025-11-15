package com.powersense.inventory.scheduling.domain.model.events;

import java.time.Instant;

public record ScheduleDisabled(
		String scheduleId,
		String deviceId,
		Instant occurredOn
) implements DomainEvent {}
