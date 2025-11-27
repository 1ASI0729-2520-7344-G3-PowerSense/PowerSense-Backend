package com.powersense.analytics.reports.application.internal.queryservices;

import com.powersense.analytics.reports.application.model.ReportKPIsResponse;
import com.powersense.analytics.reports.infrastructure.calculators.ComparisonCalculator;
import com.powersense.analytics.reports.infrastructure.calculators.ConsumptionCalculator;
import com.powersense.inventory.devices.application.internal.outboundservices.repositories.DeviceRepository;
import com.powersense.inventory.devices.domain.model.aggregates.Device;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportKPIQueryServiceImpl {

	private static final double KWH_RATE = 0.15;

	private final DeviceRepository deviceRepository;
	private final ConsumptionCalculator consumptionCalculator;
	private final ComparisonCalculator comparisonCalculator;

	public ReportKPIQueryServiceImpl(DeviceRepository deviceRepository,
			ConsumptionCalculator consumptionCalculator,
			ComparisonCalculator comparisonCalculator) {
		this.deviceRepository = deviceRepository;
		this.consumptionCalculator = consumptionCalculator;
		this.comparisonCalculator = comparisonCalculator;
	}

	public ReportKPIsResponse getKPIs() {
		List<Device> devices = deviceRepository.findAll();
		LocalDate now = LocalDate.now();
		LocalDate start = now.withDayOfMonth(1);
		double totalConsumption = consumptionCalculator.calculateTotalConsumption(devices, start, now);
		double totalCost = totalConsumption * KWH_RATE;
		int efficiency = 87;
		var comparison = comparisonCalculator.compareWithPreviousPeriod(totalConsumption);
		return new ReportKPIsResponse(totalConsumption, totalCost, efficiency, comparison);
	}
}
