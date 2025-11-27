package com.powersense.inventory.scheduling.domain.model.aggregates;

import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.scheduling.domain.model.entities.RuleAction;
import com.powersense.inventory.scheduling.domain.model.entities.RuleCondition;
import com.powersense.inventory.scheduling.domain.model.events.DomainEvent;
import com.powersense.inventory.scheduling.domain.model.events.RuleEnabled;
import com.powersense.inventory.scheduling.domain.model.events.RuleDisabled;
import com.powersense.inventory.scheduling.domain.model.events.RuleExecuted;
import com.powersense.inventory.scheduling.domain.model.valueobjects.RuleId;
import com.powersense.inventory.scheduling.domain.model.valueobjects.RuleType;
import com.powersense.inventory.scheduling.domain.services.ScheduleExecutionContext;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScheduleRule {
	private final RuleId id;
	private final RuleType type;
	private String name;
	private String description;
	private boolean enabled;
	private int priority;
	private final List<RuleCondition> conditions = new ArrayList<>();
	private final List<RuleAction> actions = new ArrayList<>();

	private final List<DomainEvent> domainEvents = new ArrayList<>();

	public ScheduleRule(RuleId id, RuleType type, String name, String description, boolean enabled, int priority) {
		this.id = Objects.requireNonNull(id, "id");
		this.type = Objects.requireNonNull(type, "type");
		this.name = Objects.requireNonNull(name, "name");
		this.description = description;
		this.enabled = enabled;
		this.priority = priority;
	}

	public void enable() {
		if (!enabled) {
			enabled = true;
			registerEvent(new RuleEnabled(id.value(), Instant.now()));
		}
	}

	public void disable() {
		if (enabled) {
			enabled = false;
			registerEvent(new RuleDisabled(id.value(), Instant.now()));
		}
	}

	public boolean shouldExecute(ScheduleExecutionContext context) {
		if (!enabled)
			return false;
		for (RuleCondition c : conditions) {
			if (!c.isMet(context))
				return false;
		}
		return true;
	}

	public void execute() {

		registerEvent(new RuleExecuted(id.value(), name, 0, Instant.now()));
	}

	public void addCondition(RuleCondition condition) {
		conditions.add(Objects.requireNonNull(condition));
	}

	public void addAction(RuleAction action) {
		actions.add(Objects.requireNonNull(action));
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public List<DomainEvent> pullDomainEvents() {
		List<DomainEvent> out = List.copyOf(domainEvents);
		domainEvents.clear();
		return out;
	}

	private void registerEvent(DomainEvent event) {
		domainEvents.add(event);
	}

	public RuleId getId() {
		return id;
	}

	public RuleType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public List<RuleCondition> getConditions() {
		return List.copyOf(conditions);
	}

	public List<RuleAction> getActions() {
		return List.copyOf(actions);
	}
}
