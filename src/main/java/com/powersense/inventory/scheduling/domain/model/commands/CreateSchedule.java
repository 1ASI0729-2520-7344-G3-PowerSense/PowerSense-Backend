package com.powersense.inventory.scheduling.domain.model.commands;

import java.util.List;

public record CreateSchedule(
		String deviceId,
		String deviceName,
		String roomName,
		boolean enabled,
		List<ScheduleEntryData> schedules
) {
	public record ScheduleEntryData(
			String action,
			TimeSlotData time,
			List<String> days
	) {}

	public record TimeSlotData(int hour, int minute) {}
}
