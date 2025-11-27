package com.powersense.inventory.scheduling.domain.model.entities;

import com.powersense.inventory.scheduling.domain.exceptions.InvalidRuleConditionException;
import com.powersense.inventory.scheduling.domain.model.valueobjects.ConditionType;
import com.powersense.inventory.scheduling.domain.services.ScheduleExecutionContext;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RuleCondition {
	private final ConditionType type;
	private final Object value;

	public RuleCondition(ConditionType type, Object value) {
		this.type = Objects.requireNonNull(type, "type");
		this.value = value;
	}

	public boolean isMet(ScheduleExecutionContext context) {
		return switch (type) {
			case TIME_RANGE -> isInTimeRange(context);
			case NO_MOTION -> noMotion(context);
			case ENERGY_THRESHOLD -> energyAbove(context);
			case TEMPERATURE -> temperatureInRange(context);
			case MOTION_DETECTED -> motionDetected(context);
		};
	}

	public String getDescription() {
		return type + " = " + String.valueOf(value);
	}

	private boolean isInTimeRange(ScheduleExecutionContext context) {
		if (value instanceof Map<?, ?> map) {
			Object start = map.get("start");
			Object end = map.get("end");
			if (start instanceof String s && end instanceof String e) {
				return within(LocalTime.parse(s), LocalTime.parse(e), context.currentTime());
			}
		}
		throw new InvalidRuleConditionException("TIME_RANGE expects map {start,end} (HH:mm)");
	}

	private boolean within(LocalTime start, LocalTime end, LocalTime now) {
		if (start.equals(end))
			return true;
		if (start.isBefore(end)) {
			return !now.isBefore(start) && !now.isAfter(end);
		} else {

			return !now.isBefore(start) || !now.isAfter(end);
		}
	}

	private boolean noMotion(ScheduleExecutionContext context) {
		if (value instanceof Number n) {
			return context.minutesWithoutMotion() >= n.intValue();
		}
		throw new InvalidRuleConditionException("NO_MOTION expects integer minutes");
	}

	private boolean energyAbove(ScheduleExecutionContext context) {
		if (value instanceof Number n) {
			return context.currentEnergyConsumption() > n.intValue();
		}
		throw new InvalidRuleConditionException("ENERGY_THRESHOLD expects integer threshold");
	}

	private boolean temperatureInRange(ScheduleExecutionContext context) {
		if (value instanceof Map<?, ?> map) {
			Integer min = map.get("min") instanceof Number n ? n.intValue() : null;
			Integer max = map.get("max") instanceof Number n ? n.intValue() : null;
			int temp = context.currentTemperature();
			if (min != null && temp < min)
				return false;
			if (max != null && temp > max)
				return false;
			return true;
		}
		throw new InvalidRuleConditionException("TEMPERATURE expects map {min,max}");
	}

	private boolean motionDetected(ScheduleExecutionContext context) {
		if (value instanceof List<?> list) {
			@SuppressWarnings("unchecked")
			List<String> roomIds = (List<String>) list;
			return context.isMotionDetectedInRooms(roomIds);
		}
		throw new InvalidRuleConditionException("MOTION_DETECTED expects List<String> roomIds");
	}

	public ConditionType getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}
}
