package com.powersense.analytics.dashboard.application.model;

public class DashboardKPIsResponse {
	private int totalDevices;
	private long activeDevices;
	private int inactiveDevices;
	private double totalConsumptionKWh;
	private double totalCostUSD;
	private int efficiencyPct;
	private double estimatedMonthlyCostUSD;
	private double monthlySavingsUSD;
	private Comparison comparison;

	public DashboardKPIsResponse(int totalDevices, long activeDevices, int inactiveDevices,
								 double totalConsumptionKWh, double totalCostUSD, int efficiencyPct,
								 double estimatedMonthlyCostUSD, double monthlySavingsUSD, Comparison comparison) {
		this.totalDevices = totalDevices;
		this.activeDevices = activeDevices;
		this.inactiveDevices = inactiveDevices;
		this.totalConsumptionKWh = totalConsumptionKWh;
		this.totalCostUSD = totalCostUSD;
		this.efficiencyPct = efficiencyPct;
		this.estimatedMonthlyCostUSD = estimatedMonthlyCostUSD;
		this.monthlySavingsUSD = monthlySavingsUSD;
		this.comparison = comparison;
	}

	public int getTotalDevices() {
		return totalDevices;
	}

	public long getActiveDevices() {
		return activeDevices;
	}

	public int getInactiveDevices() {
		return inactiveDevices;
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

	public double getEstimatedMonthlyCostUSD() {
		return estimatedMonthlyCostUSD;
	}

	public double getMonthlySavingsUSD() {
		return monthlySavingsUSD;
	}

	public Comparison getComparison() {
		return comparison;
	}
}


