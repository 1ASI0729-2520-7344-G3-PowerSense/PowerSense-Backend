package com.powersense.analytics.reports.application.model;

public class ReportKPIsResponse {
	private double totalConsumptionKWh;
	private double totalCostUSD;
	private int efficiencyPct;
	private ComparisonResponse comparison;

	public ReportKPIsResponse(double totalConsumptionKWh, double totalCostUSD, int efficiencyPct, ComparisonResponse comparison) {
		this.totalConsumptionKWh = totalConsumptionKWh;
		this.totalCostUSD = totalCostUSD;
		this.efficiencyPct = efficiencyPct;
		this.comparison = comparison;
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

	public ComparisonResponse getComparison() {
		return comparison;
	}

	public static class ComparisonResponse {
		private int consumptionPct;
		private int costPct;
		private int efficiencyPct;

		public ComparisonResponse(int consumptionPct, int costPct, int efficiencyPct) {
			this.consumptionPct = consumptionPct;
			this.costPct = costPct;
			this.efficiencyPct = efficiencyPct;
		}

		public int getConsumptionPct() {
			return consumptionPct;
		}

		public int getCostPct() {
			return costPct;
		}

		public int getEfficiencyPct() {
			return efficiencyPct;
		}
	}
}


