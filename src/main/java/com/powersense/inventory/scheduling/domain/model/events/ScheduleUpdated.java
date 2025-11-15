package com.powersense.inventory.scheduling.domain.model.events;

import java.time.Instant;

public record ScheduleUpdated(
		String scheduleId,
		Instant occurredOn
) implements DomainEvent {}
