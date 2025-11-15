package com.powersense.inventory.scheduling.domain.model.events;

import java.time.Instant;

public record RuleEnabled(
		String ruleId,
		Instant occurredOn
) implements DomainEvent {}
