package com.powersense.inventory.scheduling.domain.model.events;

import java.time.Instant;

public record ScheduleDeleted(
		String scheduleId,
		Instant occurredOn
) implements DomainEvent {}
