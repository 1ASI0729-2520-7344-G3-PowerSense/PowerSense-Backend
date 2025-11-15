package com.powersense.inventory.scheduling.domain.model.entities;

import com.powersense.inventory.scheduling.domain.model.valueobjects.DayOfWeek;
import com.powersense.inventory.scheduling.domain.model.valueobjects.ScheduleAction;
import com.powersense.inventory.scheduling.domain.model.valueobjects.TimeSlot;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ScheduleEntry {
	private final ScheduleAction action;
	private final TimeSlot time;
	private final List<DayOfWeek> days;

	public ScheduleEntry(ScheduleAction action, TimeSlot time, List<DayOfWeek> days) {
		this.action = Objects.requireNonNull(action, "action");
		this.time = Objects.requireNonNull(time, "time");
		if (days == null || days.isEmpty()) {
			throw new IllegalArgumentException("days must have at least one day");
		}
		this.days = List.copyOf(days);
	}

	public boolean appliesOnDay(DayOfWeek day) {
		return days.contains(day);
	}

	public boolean isScheduledAt(LocalTime localTime, DayOfWeek day) {
		return appliesOnDay(day) && time.toLocalTime().equals(localTime);
	}

	public String toReadableString() {
		return action + " at " + time.toFormattedString() + " on " + days;
	}

	public ScheduleAction getAction() {
		return action;
	}

	public TimeSlot getTime() {
		return time;
	}

	public List<DayOfWeek> getDays() {
		return Collections.unmodifiableList(days);
	}
}
