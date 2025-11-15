package com.powersense.inventory.scheduling.domain.model.events;

import java.time.Instant;

public record RuleExecuted(
		String ruleId,
		String ruleName,
		int affectedDevices,
		Instant occurredOn
) implements DomainEvent {}
