package com.powersense.inventory.scheduling.domain.model.valueobjects;

import com.powersense.inventory.scheduling.domain.exceptions.InvalidTimeSlotException;

import java.time.LocalTime;

public record TimeSlot(int hour, int minute) {
	public TimeSlot {
		if (hour < 0 || hour > 23) {
			throw new InvalidTimeSlotException("hour must be in [0,23]");
		}
		if (minute < 0 || minute > 59) {
			throw new InvalidTimeSlotException("minute must be in [0,59]");
		}
	}

	public boolean isBefore(TimeSlot other) {
		return this.toLocalTime().isBefore(other.toLocalTime());
	}

	public boolean isAfter(TimeSlot other) {
		return this.toLocalTime().isAfter(other.toLocalTime());
	}

	public String toFormattedString() {
		return String.format("%02d:%02d", hour, minute);
	}

	public LocalTime toLocalTime() {
		return LocalTime.of(hour, minute);
	}

	public static TimeSlot fromString(String time) {
		if (time == null || !time.matches("\\d{1,2}:\\d{2}")) {
			throw new InvalidTimeSlotException("format must be HH:mm");
		}
		String[] parts = time.split(":");
		return new TimeSlot(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
	}
}
