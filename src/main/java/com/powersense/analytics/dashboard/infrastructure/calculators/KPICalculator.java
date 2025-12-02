package com.powersense.analytics.dashboard.infrastructure.calculators;

import com.powersense.analytics.dashboard.application.model.Comparison;
import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceStatus;
import com.powersense.inventory.scheduling.domain.model.aggregates.Schedule;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KPICalculator
		implements com.powersense.analytics.dashboard.application.internal.calculators.KPICalculator {

	private static final double KWH_RATE = 0.15; // USD por kWh
	private static final int HOURS_ACTIVE_PER_DAY = 8;
	private static final int DAYS_PER_MONTH = 30;

	@Override
	public double calculateTotalConsumption(List<Device> devices) {
		// Estimación: suma de watts * horas promedio de uso diario / 1000, solo activos
		double dailyKWh = devices.stream()
				.filter(d -> d.getStatus() == DeviceStatus.ACTIVE && d.getPower() != null)
				.mapToDouble(d -> (d.getPower().watts() * (double) HOURS_ACTIVE_PER_DAY) / 1000.0)
				.sum();
		return dailyKWh * DAYS_PER_MONTH;
	}

	@Override
	public double calculateCost(double consumptionKWh) {
		return consumptionKWh * KWH_RATE;
	}

	@Override
	public int calculateEfficiency(List<Device> devices, List<Schedule> schedules) {
		long enabledSchedules = schedules.stream().filter(Schedule::isEnabled).count();
		if (devices.isEmpty())
			return 0;
		int baseEfficiency = (int) Math.round((enabledSchedules * 100.0) / devices.size());
		return Math.min(baseEfficiency, 100);
	}

	@Override
	public double calculateMonthlySavings(List<Schedule> schedules) {
		// Simplificación: cada schedule habilitado ahorra ~50 kWh/mes
		long enabledSchedules = schedules.stream().filter(Schedule::isEnabled).count();
		return enabledSchedules * 50.0 * KWH_RATE;
	}

	@Override
	public Comparison calculateComparison() {
		// Mock inicial - en producción compararía con histórico en BD
		return new Comparison(-12, -8, 3);
	}
}
