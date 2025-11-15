package com.powersense.inventory.scheduling.domain.model.valueobjects;

public enum QuickScheduleScope {
	ALL("all"),
	BEDROOMS("bedrooms"),
	COMMON_AREAS("common_areas"),
	CUSTOM("custom");

	private final String value;

	QuickScheduleScope(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
