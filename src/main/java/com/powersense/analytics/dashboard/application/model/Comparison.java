package com.powersense.analytics.dashboard.application.model;

public class Comparison {
	private double consumptionPct;
	private double costPct;
	private double efficiencyPct;

	public Comparison(double consumptionPct, double costPct, double efficiencyPct) {
		this.consumptionPct = consumptionPct;
		this.costPct = costPct;
		this.efficiencyPct = efficiencyPct;
	}

	public double getConsumptionPct() {
		return consumptionPct;
	}

	public double getCostPct() {
		return costPct;
	}

	public double getEfficiencyPct() {
		return efficiencyPct;
	}
}


