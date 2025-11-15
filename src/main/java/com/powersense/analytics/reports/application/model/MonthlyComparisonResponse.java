package com.powersense.analytics.reports.application.model;

public class MonthlyComparisonResponse {
	private String month;
	private double currentKWh;
	private double previousKWh;

	public MonthlyComparisonResponse(String month, double currentKWh, double previousKWh) {
		this.month = month;
		this.currentKWh = currentKWh;
		this.previousKWh = previousKWh;
	}

	public String getMonth() {
		return month;
	}

	public double getCurrentKWh() {
		return currentKWh;
	}

	public double getPreviousKWh() {
		return previousKWh;
	}
}


