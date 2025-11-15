package com.powersense.inventory.scheduling.domain.model.valueobjects;

public enum ConditionType {
	TIME_RANGE("time_range"),
	NO_MOTION("no_motion"),
	ENERGY_THRESHOLD("energy_threshold"),
	TEMPERATURE("temperature"),
	MOTION_DETECTED("motion_detected");

	private final String value;

	ConditionType(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
