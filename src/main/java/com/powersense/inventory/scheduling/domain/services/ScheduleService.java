package com.powersense.inventory.scheduling.domain.services;

import com.powersense.inventory.scheduling.domain.model.aggregates.Schedule;
import com.powersense.inventory.scheduling.domain.model.entities.ScheduleEntry;

import java.util.List;

public interface ScheduleService {
	void validateNoDeviceDuplication(String deviceId, String excludeScheduleId);
	void validateTimeSlotCoherence(List<ScheduleEntry> entries);
	boolean hasScheduleConflicts(Schedule schedule);
}
