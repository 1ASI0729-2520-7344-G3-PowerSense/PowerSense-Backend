package com.powersense.analytics.reports.interfaces.rest;

import com.powersense.analytics.reports.application.internal.queryservices.ReportDepartmentsQueryServiceImpl;
import com.powersense.analytics.reports.application.internal.queryservices.ReportHistoryQueryServiceImpl;
import com.powersense.analytics.reports.application.internal.queryservices.ReportKPIQueryServiceImpl;
import com.powersense.analytics.reports.application.internal.queryservices.ReportMonthlyQueryServiceImpl;
import com.powersense.analytics.reports.interfaces.rest.resources.DepartmentMetricResponse;
import com.powersense.analytics.reports.interfaces.rest.resources.MonthlyComparisonResponse;
import com.powersense.analytics.reports.interfaces.rest.resources.RealtimeConsumptionResponse;
import com.powersense.analytics.reports.interfaces.rest.resources.ReportKPIsResponse;
import com.powersense.analytics.reports.interfaces.rest.resources.ReportHistoryResponse;
import com.powersense.analytics.reports.interfaces.rest.transform.ReportAssembler;
import com.powersense.inventory.devices.application.internal.outboundservices.repositories.DeviceRepository;
import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/analytics/reports")
@CrossOrigin
public class ReportController {

	private final ReportKPIQueryServiceImpl reportKPIQueryService;
	private final ReportMonthlyQueryServiceImpl reportMonthlyQueryService;
	private final ReportDepartmentsQueryServiceImpl reportDepartmentsQueryService;
	private final ReportHistoryQueryServiceImpl reportHistoryQueryService;
	private final ReportAssembler reportAssembler;
	private final DeviceRepository deviceRepository;

	public ReportController(ReportKPIQueryServiceImpl reportKPIQueryService,
							ReportMonthlyQueryServiceImpl reportMonthlyQueryService,
							ReportDepartmentsQueryServiceImpl reportDepartmentsQueryService,
							ReportHistoryQueryServiceImpl reportHistoryQueryService,
							ReportAssembler reportAssembler,
							DeviceRepository deviceRepository) {
		this.reportKPIQueryService = reportKPIQueryService;
		this.reportMonthlyQueryService = reportMonthlyQueryService;
		this.reportDepartmentsQueryService = reportDepartmentsQueryService;
		this.reportHistoryQueryService = reportHistoryQueryService;
		this.reportAssembler = reportAssembler;
		this.deviceRepository = deviceRepository;
	}

	@GetMapping("/kpis")
	public ResponseEntity<ReportKPIsResponse> getReportKPIs() {
		var model = reportKPIQueryService.getKPIs();
		return ResponseEntity.ok(reportAssembler.toKPIsResource(model));
	}

	@GetMapping("/monthly-comparison")
	public ResponseEntity<List<MonthlyComparisonResponse>> getMonthlyComparison() {
		var models = reportMonthlyQueryService.getMonthlyComparison();
		return ResponseEntity.ok(reportAssembler.toMonthlyList(models));
	}

	@GetMapping("/departments")
	public ResponseEntity<List<DepartmentMetricResponse>> getDepartmentMetrics() {
		var models = reportDepartmentsQueryService.getDepartmentMetrics();
		return ResponseEntity.ok(reportAssembler.toDepartmentList(models));
	}

	@GetMapping("/history")
	public ResponseEntity<List<ReportHistoryResponse>> getReportHistory() {
		var models = reportHistoryQueryService.getReportHistory();
		return ResponseEntity.ok(reportAssembler.toHistoryList(models));
	}

	@GetMapping("/realtime-consumption")
	public ResponseEntity<List<RealtimeConsumptionResponse>> getRealtimeConsumption(
			@RequestParam(required = false) String period) {
		List<Device> activeDevices = deviceRepository.findAll().stream()
				.filter(d -> d.getStatus() == DeviceStatus.ACTIVE)
				.collect(Collectors.toList());

		List<RealtimeConsumptionResponse> result = new ArrayList<>();

		if (period == null || period.equals("day")) {
			for (int hour = 0; hour < 24; hour++) {
				double consumption = calculateConsumptionForHour(activeDevices, hour);
				result.add(new RealtimeConsumptionResponse("day", String.format("%02d:00", hour), consumption));
			}
		}

		if (period == null || period.equals("week")) {
			String[] days = {"Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom"};
			for (String day : days) {
				double consumption = calculateConsumptionForWeekday(activeDevices, day);
				result.add(new RealtimeConsumptionResponse("week", day, consumption));
			}
		}

		if (period == null || period.equals("month")) {
			for (int day = 1; day <= 31; day++) {
				double consumption = calculateConsumptionForMonthDay(activeDevices, day);
				result.add(new RealtimeConsumptionResponse("month", String.format("%02d", day), consumption));
			}
		}

		return ResponseEntity.ok(result);
	}

	private double calculateConsumptionForHour(List<Device> devices, int hour) {
		double base = devices.stream().mapToDouble(d -> d.getPower().watts()).sum() / 1000.0;
		double factor = (hour >= 18 && hour <= 23) ? 1.2 : (hour >= 0 && hour <= 6) ? 0.8 : 1.0;
		return base * factor;
	}

	private double calculateConsumptionForWeekday(List<Device> devices, String day) {
		double base = devices.stream().mapToDouble(d -> d.getPower().watts()).sum() / 1000.0;
		double factor = switch (day) {
			case "Sab" -> 0.9;
			case "Dom" -> 0.85;
			default -> 1.0;
		};
		return base * factor;
	}

	private double calculateConsumptionForMonthDay(List<Device> devices, int day) {
		double base = devices.stream().mapToDouble(d -> d.getPower().watts()).sum() / 1000.0;
		double factor = 0.9 + ((day % 10) / 100.0);
		return base * factor;
	}
}
