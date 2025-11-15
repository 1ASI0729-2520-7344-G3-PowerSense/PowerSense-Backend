package com.powersense.inventory.scheduling.domain.model.valueobjects;

public enum ActionType {
	TURN_OFF("turn_off"),
	TURN_ON("turn_on"),
	TURN_OFF_ALL("turn_off_all"),
	TURN_OFF_CATEGORY("turn_off_category"),
	DIM_CATEGORY("dim_category"),
	DISABLE_CATEGORY("disable_category"),
	ADJUST_TEMPERATURE("adjust_temperature");

	private final String value;

	ActionType(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
