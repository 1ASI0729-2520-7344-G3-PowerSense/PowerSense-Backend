package com.powersense.inventory.scheduling.application.internal.outboundservices.repositories;

import com.powersense.inventory.devices.domain.model.valueobjects.DeviceId;
import com.powersense.inventory.scheduling.domain.model.aggregates.Schedule;
import com.powersense.inventory.scheduling.domain.model.valueobjects.ScheduleId;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {
	Optional<Schedule> findById(ScheduleId id);
	Optional<Schedule> findByDeviceId(DeviceId deviceId);
	List<Schedule> findAll();
	Schedule save(Schedule schedule);
	void saveAll(List<Schedule> schedules);
	void deleteById(ScheduleId id);
	ScheduleId nextIdentity();
}


