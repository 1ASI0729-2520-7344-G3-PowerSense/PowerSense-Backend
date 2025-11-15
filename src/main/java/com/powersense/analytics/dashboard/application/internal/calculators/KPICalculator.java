package com.powersense.analytics.dashboard.application.internal.calculators;

import com.powersense.analytics.dashboard.application.model.Comparison;
import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.scheduling.domain.model.aggregates.Schedule;

import java.util.List;

public interface KPICalculator {
	double calculateTotalConsumption(List<Device> devices);
	double calculateCost(double totalConsumptionKWh);
	int calculateEfficiency(List<Device> devices, List<Schedule> schedules);
	double calculateMonthlySavings(List<Schedule> schedules);
	Comparison calculateComparison();
}


