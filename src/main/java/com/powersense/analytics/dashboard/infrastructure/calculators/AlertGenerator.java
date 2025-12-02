package com.powersense.analytics.dashboard.infrastructure.calculators;

import com.powersense.analytics.dashboard.application.model.DashboardAlertResponse;
import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceStatus;
import com.powersense.inventory.scheduling.domain.model.aggregates.Schedule;
import com.powersense.inventory.scheduling.domain.model.entities.ScheduleEntry;
import com.powersense.inventory.scheduling.domain.model.valueobjects.DayOfWeek;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class AlertGenerator {

	public List<DashboardAlertResponse> generateAlerts(List<Device> devices, List<Schedule> schedules) {
		List<DashboardAlertResponse> alerts = new ArrayList<>();

		List<Device> highConsumption = devices.stream()
				.filter(this::isHighConsumption)
				.toList();
		if (!highConsumption.isEmpty()) {
			alerts.add(new DashboardAlertResponse(
					"alert-" + UUID.randomUUID(),
					"error",
					"error",
					"Consumo excesivo detectado en " + highConsumption.size() + " dispositivos",
					Instant.now()));
		}

		long disabled = schedules.stream().filter(s -> !s.isEnabled()).count();
		if (disabled > 0) {
			alerts.add(new DashboardAlertResponse(
					"alert-" + UUID.randomUUID(),
					"warning",
					"warning",
					disabled + " programaciones deshabilitadas",
					Instant.now()));
		}

		alerts.add(new DashboardAlertResponse(
				"alert-" + UUID.randomUUID(),
				"info",
				"check_circle",
				"Sistema funcionando correctamente",
				Instant.now()));

		return alerts.stream().limit(5).toList();
	}

	public boolean isHighConsumption(Device device) {
		return device.getStatus() == DeviceStatus.ACTIVE && device.getPower() != null
				&& device.getPower().watts() > 500;
	}

	public boolean shouldBeActive(Device device, LocalTime currentTime, DayOfWeek currentDay,
			List<Schedule> schedules) {
		return schedules.stream()
				.filter(Schedule::isEnabled)
				.filter(s -> s.getDeviceId().value().equals(device.getId().value()))
				.flatMap(s -> s.getEntries().stream())
				.anyMatch(e -> isEntryOn(e, currentTime, currentDay));
	}

	private boolean isEntryOn(ScheduleEntry e, LocalTime time, DayOfWeek day) {
		return e.getDays().contains(day) && e.getTime().toLocalTime().equals(time) && e.getAction().isOn();
	}
}
