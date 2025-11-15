package com.powersense.inventory.scheduling.domain.model.valueobjects;

public enum RuleType {
	NIGHT_MODE("night_mode"),
	ENERGY_SAVING("energy_saving"),
	AWAY_MODE("away_mode"),
	WELCOME_HOME("welcome_home"),
	PEAK_HOURS_OPTIMIZATION("peak_hours_optimization");

	private final String value;

	RuleType(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
