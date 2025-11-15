package com.powersense.inventory.scheduling.domain.model.valueobjects;

public enum DayOfWeek {
	MONDAY("monday", "lunes"),
	TUESDAY("tuesday", "martes"),
	WEDNESDAY("wednesday", "miércoles"),
	THURSDAY("thursday", "jueves"),
	FRIDAY("friday", "viernes"),
	SATURDAY("saturday", "sábado"),
	SUNDAY("sunday", "domingo");

	private final String value;
	private final String spanish;

	DayOfWeek(String value, String spanish) {
		this.value = value;
		this.spanish = spanish;
	}

	public boolean isWeekday() {
		return this == MONDAY || this == TUESDAY || this == WEDNESDAY || this == THURSDAY || this == FRIDAY;
	}

	public boolean isWeekend() {
		return this == SATURDAY || this == SUNDAY;
	}

	public String toSpanish() {
		return spanish;
	}

	public static DayOfWeek fromString(String day) {
		if (day == null) throw new IllegalArgumentException("day cannot be null");
		String d = day.trim().toLowerCase();
		for (DayOfWeek v : DayOfWeek.values()) {
			if (v.name().equalsIgnoreCase(d) || v.value.equalsIgnoreCase(d) || v.spanish.equalsIgnoreCase(d)) {
				return v;
			}
		}
		throw new IllegalArgumentException("Unknown day: " + day);
	}

	@Override
	public String toString() {
		return value;
	}
}
