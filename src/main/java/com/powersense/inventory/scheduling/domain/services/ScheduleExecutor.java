package com.powersense.inventory.scheduling.domain.services;

import com.powersense.inventory.scheduling.domain.model.aggregates.ScheduleRule;

public interface ScheduleExecutor {
	void executeScheduledActions();
	void evaluateRules();
	void executeRule(ScheduleRule rule);
}
