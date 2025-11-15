package com.powersense.analytics.reports.infrastructure.calculators;

import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ConsumptionCalculator {

	public double calculateDeviceConsumption(Device device, int hoursPerDay, int days) {
		return (device.getPower().watts() * (double) hoursPerDay / 1000.0) * days;
	}

	public Map<String, Double> calculateConsumptionByRoom(List<Device> devices) {
		int days = 30;
		int hours = 8;
		return devices.stream()
				.filter(d -> d.getStatus() == DeviceStatus.ACTIVE)
				.collect(Collectors.groupingBy(
						d -> d.getLocation().roomId().value(),
						Collectors.summingDouble(d -> calculateDeviceConsumption(d, hours, days))
				));
	}

	public double calculateTotalConsumption(List<Device> devices, LocalDate from, LocalDate to) {
		int days = Math.max(1, (int) (to.toEpochDay() - from.toEpochDay() + 1));
		int hours = 8;
		return devices.stream()
				.filter(d -> d.getStatus() == DeviceStatus.ACTIVE)
				.mapToDouble(d -> calculateDeviceConsumption(d, hours, days))
				.sum();
	}
}
