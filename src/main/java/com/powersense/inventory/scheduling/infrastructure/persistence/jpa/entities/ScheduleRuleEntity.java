package com.powersense.inventory.scheduling.infrastructure.persistence.jpa.entities;

import com.powersense.inventory.scheduling.domain.model.aggregates.ScheduleRule;
import com.powersense.inventory.scheduling.domain.model.entities.RuleAction;
import com.powersense.inventory.scheduling.domain.model.entities.RuleCondition;
import com.powersense.inventory.scheduling.domain.model.valueobjects.RuleId;
import com.powersense.inventory.scheduling.domain.model.valueobjects.RuleType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "schedule_rules")
public class ScheduleRuleEntity {
	@Id
	private String id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RuleType type;

	@Column(nullable = false)
	private String name;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(nullable = false)
	private boolean enabled;

	@Column(nullable = false, unique = true)
	private int priority;

	@OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RuleConditionEntity> conditions = new ArrayList<>();

	@OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RuleActionEntity> actions = new ArrayList<>();

	public static ScheduleRuleEntity fromDomain(ScheduleRule rule) {
		ScheduleRuleEntity e = new ScheduleRuleEntity();
		e.id = rule.getId().value();
		e.type = rule.getType();
		e.name = rule.getName();
		e.description = rule.getDescription();
		e.enabled = rule.isEnabled();
		e.priority = rule.getPriority();
		e.conditions = rule.getConditions().stream().map(RuleConditionEntity::fromDomain).collect(Collectors.toList());
		e.actions = rule.getActions().stream().map(RuleActionEntity::fromDomain).collect(Collectors.toList());
		e.conditions.forEach(c -> c.setRule(e));
		e.actions.forEach(a -> a.setRule(e));
		return e;
	}

	public ScheduleRule toDomain() {
		ScheduleRule domain = new ScheduleRule(new RuleId(id), type, name, description, enabled, priority);
		for (RuleConditionEntity c : conditions) {
			RuleCondition cond = c.toDomain();
			domain.addCondition(cond);
		}
		for (RuleActionEntity a : actions) {
			RuleAction act = a.toDomain();
			domain.addAction(act);
		}
		return domain;
	}
}


