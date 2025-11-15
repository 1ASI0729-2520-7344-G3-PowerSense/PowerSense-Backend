package com.powersense.analytics.reports.interfaces.rest.transform;

import com.powersense.analytics.reports.interfaces.rest.resources.DepartmentMetricResponse;
import com.powersense.analytics.reports.interfaces.rest.resources.MonthlyComparisonResponse;
import com.powersense.analytics.reports.interfaces.rest.resources.ReportHistoryResponse;
import com.powersense.analytics.reports.interfaces.rest.resources.ReportKPIsResponse;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class ReportAssembler {

	public ReportKPIsResponse toKPIsResource(com.powersense.analytics.reports.application.model.ReportKPIsResponse model) {
		ReportKPIsResponse r = new ReportKPIsResponse();
		r.setTotalConsumptionKWh(model.getTotalConsumptionKWh());
		r.setTotalCostUSD(model.getTotalCostUSD());
		r.setEfficiencyPct(model.getEfficiencyPct());
		ReportKPIsResponse.ComparisonResponse cr = new ReportKPIsResponse.ComparisonResponse();
		cr.setConsumptionPct(model.getComparison().getConsumptionPct());
		cr.setCostPct(model.getComparison().getCostPct());
		cr.setEfficiencyPct(model.getComparison().getEfficiencyPct());
		r.setComparison(cr);
		return r;
	}

	public List<MonthlyComparisonResponse> toMonthlyList(List<com.powersense.analytics.reports.application.model.MonthlyComparisonResponse> models) {
		return models.stream().map(m -> {
			MonthlyComparisonResponse r = new MonthlyComparisonResponse();
			r.setMonth(m.getMonth());
			r.setY2024((int) Math.round(m.getCurrentKWh()));
			r.setY2023((int) Math.round(m.getPreviousKWh()));
			return r;
		}).collect(Collectors.toList());
	}

	public List<DepartmentMetricResponse> toDepartmentList(List<com.powersense.analytics.reports.application.model.DepartmentMetricResponse> models) {
		return models.stream().map(m -> {
			DepartmentMetricResponse r = new DepartmentMetricResponse();
			r.setDepartmentId(m.getRoomId());
			r.setDepartmentName(m.getRoomName());
			r.setMetric(m.getMetricType());
			r.setValue(m.getValue());
			return r;
		}).collect(Collectors.toList());
	}

	public List<ReportHistoryResponse> toHistoryList(List<com.powersense.analytics.reports.application.model.ReportHistoryResponse> models) {
		return models.stream().map(m -> {
			ReportHistoryResponse r = new ReportHistoryResponse();
			r.setId(m.getId());
			r.setDate(m.getDate().toString());
			r.setName(m.getTitle());
			r.setType(m.getType());
			r.setDepartment(m.getDepartment());
			r.setConsumptionKWh(m.getTotalConsumptionKWh());
			r.setCost(m.getTotalCostUSD());
			r.setVariationPct(m.getEfficiencyPct());
			return r;
		}).collect(Collectors.toList());
	}
}
