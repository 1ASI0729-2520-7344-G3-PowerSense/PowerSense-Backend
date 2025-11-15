package com.powersense.inventory.scheduling.domain.model.valueobjects;

public enum ScheduleAction {
	ON("on"), OFF("off");

	private final String value;

	ScheduleAction(String value) {
		this.value = value;
	}

	public boolean isOn() {
		return this == ON;
	}

	public boolean isOff() {
		return this == OFF;
	}

	public ScheduleAction toggle() {
		return this == ON ? OFF : ON;
	}

	@Override
	public String toString() {
		return value;
	}
}
