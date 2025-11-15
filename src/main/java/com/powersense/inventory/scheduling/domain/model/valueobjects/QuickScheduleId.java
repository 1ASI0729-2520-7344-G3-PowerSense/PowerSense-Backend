package com.powersense.inventory.scheduling.domain.model.valueobjects;

public record QuickScheduleId(String value) {
	public QuickScheduleId {
		if (value == null || value.trim().isEmpty()) {
			throw new IllegalArgumentException("QuickScheduleId cannot be null or empty");
		}
	}

	@Override
	public String toString() {
		return value;
	}
}
