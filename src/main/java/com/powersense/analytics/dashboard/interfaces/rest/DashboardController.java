package com.powersense.analytics.dashboard.interfaces.rest;

import com.powersense.analytics.dashboard.application.internal.queryservices.DashboardAlertsQueryServiceImpl;
import com.powersense.analytics.dashboard.application.internal.queryservices.DashboardKPIQueryServiceImpl;
import com.powersense.analytics.dashboard.application.internal.queryservices.DashboardTipsQueryServiceImpl;
import com.powersense.analytics.dashboard.application.model.Comparison;
import com.powersense.analytics.dashboard.interfaces.rest.resources.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/analytics/dashboard")
@CrossOrigin
public class DashboardController {

	private final DashboardKPIQueryServiceImpl kpiService;
	private final DashboardAlertsQueryServiceImpl alertsService;
	private final DashboardTipsQueryServiceImpl tipsService;

	public DashboardController(DashboardKPIQueryServiceImpl kpiService,
							   DashboardAlertsQueryServiceImpl alertsService,
							   DashboardTipsQueryServiceImpl tipsService) {
		this.kpiService = kpiService;
		this.alertsService = alertsService;
		this.tipsService = tipsService;
	}

	@GetMapping("/kpis")
	public ResponseEntity<DashboardKPIsResponse> getDashboardKPIs() {
		com.powersense.analytics.dashboard.application.model.DashboardKPIsResponse model = kpiService.getKPIs();
		return ResponseEntity.ok(toResource(model));
	}

	@GetMapping("/alerts")
	public ResponseEntity<List<DashboardAlertResponse>> getRecentAlerts() {
		List<com.powersense.analytics.dashboard.application.model.DashboardAlertResponse> alerts = alertsService.getRecentAlerts();
		return ResponseEntity.ok(alerts.stream().map(this::toResource).collect(Collectors.toList()));
	}

	@GetMapping("/tips")
	public ResponseEntity<List<DashboardTipResponse>> getSavingTips() {
		List<com.powersense.analytics.dashboard.application.model.DashboardTipResponse> tips = tipsService.getTips();
		return ResponseEntity.ok(tips.stream().map(this::toResource).collect(Collectors.toList()));
	}

	private DashboardKPIsResponse toResource(com.powersense.analytics.dashboard.application.model.DashboardKPIsResponse m) {
		DashboardKPIsResponse r = new DashboardKPIsResponse();
		r.setTotalDevices(m.getTotalDevices());
		r.setActiveDevices(m.getActiveDevices());
		r.setInactiveDevices(m.getInactiveDevices());
		r.setTotalConsumptionKWh(m.getTotalConsumptionKWh());
		r.setTotalCostUSD(m.getTotalCostUSD());
		r.setEfficiencyPct(m.getEfficiencyPct());
		r.setEstimatedMonthlyCost(m.getEstimatedMonthlyCostUSD());
		r.setMonthlySavings(m.getMonthlySavingsUSD());
		r.setComparison(toResource(m.getComparison()));
		return r;
	}

	private DashboardKPIsResponse.ComparisonResponse toResource(Comparison c) {
		DashboardKPIsResponse.ComparisonResponse r = new DashboardKPIsResponse.ComparisonResponse();
		r.setConsumptionPct((int) Math.round(c.getConsumptionPct()));
		r.setCostPct((int) Math.round(c.getCostPct()));
		r.setEfficiencyPct((int) Math.round(c.getEfficiencyPct()));
		return r;
	}

	private DashboardAlertResponse toResource(com.powersense.analytics.dashboard.application.model.DashboardAlertResponse a) {
		DashboardAlertResponse r = new DashboardAlertResponse();
		r.setId(a.getId());
		r.setType(a.getSeverity());
		r.setIcon(a.getIcon());
		r.setMessage(a.getMessage());
		r.setTimestamp(a.getTimestamp().toString());
		return r;
	}

	private DashboardTipResponse toResource(com.powersense.analytics.dashboard.application.model.DashboardTipResponse t) {
		DashboardTipResponse r = new DashboardTipResponse();
		r.setId(t.getId());
		r.setMessage(t.getMessage());
		r.setCategory(t.getCategory());
		return r;
	}
}
