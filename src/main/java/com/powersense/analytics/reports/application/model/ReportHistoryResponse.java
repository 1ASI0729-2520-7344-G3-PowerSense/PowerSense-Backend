package com.powersense.analytics.reports.application.model;

import java.time.LocalDate;

public class ReportHistoryResponse {
	private String id;
	private LocalDate date;
	private String title;
	private String type;
	private String department;
	private double totalConsumptionKWh;
	private double totalCostUSD;
	private int efficiencyPct;

	public ReportHistoryResponse(String id, LocalDate date, String title, String type, String department,
								 double totalConsumptionKWh, double totalCostUSD, int efficiencyPct) {
		this.id = id;
		this.date = date;
		this.title = title;
		this.type = type;
		this.department = department;
		this.totalConsumptionKWh = totalConsumptionKWh;
		this.totalCostUSD = totalCostUSD;
		this.efficiencyPct = efficiencyPct;
	}

	public String getId() {
		return id;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getTitle() {
		return title;
	}

	public String getType() {
		return type;
	}

	public String getDepartment() {
		return department;
	}

	public double getTotalConsumptionKWh() {
		return totalConsumptionKWh;
	}

	public double getTotalCostUSD() {
		return totalCostUSD;
	}

	public int getEfficiencyPct() {
		return efficiencyPct;
	}
}


