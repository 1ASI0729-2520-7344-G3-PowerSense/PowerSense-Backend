package com.powersense.inventory.scheduling.application.internal.outboundservices.repositories;

import com.powersense.inventory.scheduling.domain.model.aggregates.ScheduleRule;
import com.powersense.inventory.scheduling.domain.model.valueobjects.RuleId;

import java.util.List;
import java.util.Optional;

public interface ScheduleRuleRepository {
	Optional<ScheduleRule> findById(RuleId id);
	List<ScheduleRule> findAll();
	ScheduleRule save(ScheduleRule rule);
}


