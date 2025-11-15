package com.powersense.inventory.scheduling.domain.model.valueobjects;

public record RuleId(String value) {
	public RuleId {
		if (value == null || value.trim().isEmpty()) {
			throw new IllegalArgumentException("RuleId cannot be null or empty");
		}
	}

	@Override
	public String toString() {
		return value;
	}
}
