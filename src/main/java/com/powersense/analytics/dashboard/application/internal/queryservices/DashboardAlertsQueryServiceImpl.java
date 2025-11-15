package com.powersense.analytics.dashboard.application.internal.queryservices;

import com.powersense.analytics.dashboard.application.model.DashboardAlertResponse;
import com.powersense.analytics.dashboard.infrastructure.calculators.AlertGenerator;
import com.powersense.inventory.devices.application.internal.outboundservices.repositories.DeviceRepository;
import com.powersense.inventory.scheduling.application.internal.outboundservices.repositories.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardAlertsQueryServiceImpl {

	private final DeviceRepository deviceRepository;
	private final ScheduleRepository scheduleRepository;
	private final AlertGenerator alertGenerator;

	public DashboardAlertsQueryServiceImpl(DeviceRepository deviceRepository,
										   ScheduleRepository scheduleRepository,
										   AlertGenerator alertGenerator) {
		this.deviceRepository = deviceRepository;
		this.scheduleRepository = scheduleRepository;
		this.alertGenerator = alertGenerator;
	}

	public List<DashboardAlertResponse> getRecentAlerts() {
		return alertGenerator.generateAlerts(
				deviceRepository.findAll(),
				scheduleRepository.findAll()
		);
	}
}
