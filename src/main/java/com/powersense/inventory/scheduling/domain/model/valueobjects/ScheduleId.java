package com.powersense.inventory.scheduling.domain.model.valueobjects;

public record ScheduleId(String value) {
	public ScheduleId {
		if (value == null || value.trim().isEmpty()) {
			throw new IllegalArgumentException("ScheduleId cannot be null or empty");
		}
	}

	@Override
	public String toString() {
		return value;
	}
}
