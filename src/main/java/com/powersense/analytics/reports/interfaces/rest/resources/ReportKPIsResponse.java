package com.powersense.analytics.reports.interfaces.rest.resources;

public class ReportKPIsResponse {
	private double totalConsumptionKWh;
	private double totalCostUSD;
	private int efficiencyPct;
	private ComparisonResponse comparison;

	public double getTotalConsumptionKWh() {
		return totalConsumptionKWh;
	}

	public void setTotalConsumptionKWh(double totalConsumptionKWh) {
		this.totalConsumptionKWh = totalConsumptionKWh;
	}

	public double getTotalCostUSD() {
		return totalCostUSD;
	}

	public void setTotalCostUSD(double totalCostUSD) {
		this.totalCostUSD = totalCostUSD;
	}

	public int getEfficiencyPct() {
		return efficiencyPct;
	}

	public void setEfficiencyPct(int efficiencyPct) {
		this.efficiencyPct = efficiencyPct;
	}

	public ComparisonResponse getComparison() {
		return comparison;
	}

	public void setComparison(ComparisonResponse comparison) {
		this.comparison = comparison;
	}

	public static class ComparisonResponse {
		private int consumptionPct;
		private int costPct;
		private int efficiencyPct;

		public int getConsumptionPct() {
			return consumptionPct;
		}

		public void setConsumptionPct(int consumptionPct) {
			this.consumptionPct = consumptionPct;
		}

		public int getCostPct() {
			return costPct;
		}

		public void setCostPct(int costPct) {
			this.costPct = costPct;
		}

		public int getEfficiencyPct() {
			return efficiencyPct;
		}

		public void setEfficiencyPct(int efficiencyPct) {
			this.efficiencyPct = efficiencyPct;
		}
	}
}
