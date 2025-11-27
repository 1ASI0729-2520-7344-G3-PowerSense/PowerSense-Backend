package com.powersense.analytics.alerts.interfaces.rest;

import com.powersense.analytics.alerts.application.internal.commandservices.AlertCommandServiceImpl;
import com.powersense.analytics.alerts.application.internal.queryservices.AlertQueryServiceImpl;
import com.powersense.analytics.alerts.domain.model.aggregates.Alert;
import com.powersense.analytics.alerts.domain.model.commands.AcknowledgeAlert;
import com.powersense.analytics.alerts.domain.model.queries.GetAlertById;
import com.powersense.analytics.alerts.domain.model.queries.ListAlerts;
import com.powersense.analytics.alerts.interfaces.rest.resources.AlertResponse;
import com.powersense.analytics.alerts.interfaces.rest.resources.CreateAlertResource;
import com.powersense.analytics.alerts.interfaces.rest.transform.AlertAssembler;
import com.powersense.analytics.dashboard.application.internal.queryservices.DashboardAlertsQueryServiceImpl;
import com.powersense.analytics.dashboard.interfaces.rest.resources.DashboardAlertResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/analytics/alerts")
@CrossOrigin
public class AlertController {

	private final DashboardAlertsQueryServiceImpl dashboardAlertsQueryService;
	private final AlertCommandServiceImpl alertCommandService;
	private final AlertQueryServiceImpl alertQueryService;
	private final AlertAssembler alertAssembler;

	public AlertController(DashboardAlertsQueryServiceImpl dashboardAlertsQueryService,
						   AlertCommandServiceImpl alertCommandService,
						   AlertQueryServiceImpl alertQueryService,
						   AlertAssembler alertAssembler) {
		this.dashboardAlertsQueryService = dashboardAlertsQueryService;
		this.alertCommandService = alertCommandService;
		this.alertQueryService = alertQueryService;
		this.alertAssembler = alertAssembler;
	}

	// Existing endpoint - dynamic alerts
	@GetMapping("/recent")
	public ResponseEntity<List<DashboardAlertResponse>> getRecentAlerts() {
		var alerts = dashboardAlertsQueryService.getRecentAlerts();
		return ResponseEntity.ok(
				alerts.stream().map(a -> {
					DashboardAlertResponse r = new DashboardAlertResponse();
					r.setId(a.getId());
					r.setType(a.getSeverity());
					r.setIcon(a.getIcon());
					r.setMessage(a.getMessage());
					r.setTimestamp(a.getTimestamp().toString());
					return r;
				}).collect(Collectors.toList())
		);
	}

	// New endpoints - persistent alerts
	@PostMapping
	public ResponseEntity<AlertResponse> createAlert(@Valid @RequestBody CreateAlertResource resource) {
		try {
			Alert created = alertCommandService.createAlert(alertAssembler.toCreateCommand(resource));
			return ResponseEntity.status(HttpStatus.CREATED).body(alertAssembler.toResponse(created));
		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping
	public ResponseEntity<List<AlertResponse>> listAlerts(
			@RequestParam(required = false) String type,
			@RequestParam(required = false) String severity,
			@RequestParam(required = false) String deviceId,
			@RequestParam(required = false) Boolean acknowledged,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate
	) {
		ListAlerts query = new ListAlerts(type, severity, deviceId, acknowledged, startDate, endDate);
		List<Alert> alerts = alertQueryService.listAlerts(query);
		return ResponseEntity.ok(alertAssembler.toResponseList(alerts));
	}

	@GetMapping("/{id}")
	public ResponseEntity<AlertResponse> getAlertById(@PathVariable String id) {
		try {
			Alert alert = alertQueryService.getAlertById(new GetAlertById(id));
			return ResponseEntity.ok(alertAssembler.toResponse(alert));
		} catch (Exception ex) {
			return ResponseEntity.notFound().build();
		}
	}

	@PatchMapping("/{id}/acknowledge")
	public ResponseEntity<AlertResponse> acknowledgeAlert(@PathVariable String id) {
		try {
			Alert acknowledged = alertCommandService.acknowledgeAlert(new AcknowledgeAlert(id));
			return ResponseEntity.ok(alertAssembler.toResponse(acknowledged));
		} catch (Exception ex) {
			return ResponseEntity.notFound().build();
		}
	}
}
