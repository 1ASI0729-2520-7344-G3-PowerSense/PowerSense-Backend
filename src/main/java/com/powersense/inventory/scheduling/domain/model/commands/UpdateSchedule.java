package com.powersense.inventory.scheduling.domain.model.commands;

import java.util.List;

public record UpdateSchedule(
		String scheduleId,
		Boolean enabled,
		List<CreateSchedule.ScheduleEntryData> schedules
) {}
