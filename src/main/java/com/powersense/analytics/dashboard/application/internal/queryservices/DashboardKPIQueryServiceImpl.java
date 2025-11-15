package com.powersense.analytics.dashboard.application.internal.queryservices;

import com.powersense.analytics.dashboard.application.internal.calculators.KPICalculator;
import com.powersense.analytics.dashboard.application.model.DashboardKPIsResponse;
import com.powersense.inventory.devices.application.internal.outboundservices.repositories.DeviceRepository;
import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceStatus;
import com.powersense.inventory.scheduling.application.internal.outboundservices.repositories.ScheduleRepository;
import com.powersense.inventory.scheduling.domain.model.aggregates.Schedule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardKPIQueryServiceImpl {

	private final DeviceRepository deviceRepository;
	private final ScheduleRepository scheduleRepository;
	private final KPICalculator kpiCalculator;

	public DashboardKPIQueryServiceImpl(DeviceRepository deviceRepository,
										ScheduleRepository scheduleRepository,
										KPICalculator kpiCalculator) {
		this.deviceRepository = deviceRepository;
		this.scheduleRepository = scheduleRepository;
		this.kpiCalculator = kpiCalculator;
	}

	public DashboardKPIsResponse getKPIs() {
		List<Device> devices = deviceRepository.findAll();

		int totalDevices = devices.size();
		long activeDevices = devices.stream().filter(d -> d.getStatus() == DeviceStatus.ACTIVE).count();
		int inactiveDevices = totalDevices - (int) activeDevices;

		double totalConsumptionKWh = kpiCalculator.calculateTotalConsumption(devices);
		double totalCostUSD = kpiCalculator.calculateCost(totalConsumptionKWh);

		List<Schedule> schedules = scheduleRepository.findAll();
		int efficiencyPct = kpiCalculator.calculateEfficiency(devices, schedules);
		double monthlySavings = kpiCalculator.calculateMonthlySavings(schedules);

		return new DashboardKPIsResponse(
				totalDevices,
				activeDevices,
				inactiveDevices,
				totalConsumptionKWh,
				totalCostUSD,
				efficiencyPct,
				totalCostUSD * 1.2,
				monthlySavings,
				kpiCalculator.calculateComparison()
		);
	}
}
