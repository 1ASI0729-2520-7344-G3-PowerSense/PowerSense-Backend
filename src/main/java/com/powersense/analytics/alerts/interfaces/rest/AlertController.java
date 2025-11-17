package com.powersense.analytics.alerts.interfaces.rest;

import com.powersense.analytics.dashboard.application.internal.queryservices.DashboardAlertsQueryServiceImpl;
import com.powersense.analytics.dashboard.interfaces.rest.resources.DashboardAlertResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/analytics/alerts")
@CrossOrigin
public class AlertController {

	private final DashboardAlertsQueryServiceImpl dashboardAlertsQueryService;

	public AlertController(DashboardAlertsQueryServiceImpl dashboardAlertsQueryService) {
		this.dashboardAlertsQueryService = dashboardAlertsQueryService;
	}

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
}
