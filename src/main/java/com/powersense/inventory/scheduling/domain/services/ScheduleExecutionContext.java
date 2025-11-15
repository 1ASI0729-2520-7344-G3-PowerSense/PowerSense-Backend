package com.powersense.inventory.scheduling.domain.services;

import com.powersense.inventory.devices.domain.model.aggregates.Device;

import java.time.LocalTime;
import java.util.List;

public interface ScheduleExecutionContext {
	LocalTime currentTime();
	boolean isMotionDetectedInRooms(List<String> roomIds);
	int minutesWithoutMotion();
	int currentEnergyConsumption();
	int currentTemperature();
	List<Device> allDevices();
}


