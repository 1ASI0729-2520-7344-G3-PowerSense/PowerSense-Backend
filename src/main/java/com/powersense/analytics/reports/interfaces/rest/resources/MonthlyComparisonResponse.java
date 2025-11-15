package com.powersense.analytics.reports.interfaces.rest.resources;

public class MonthlyComparisonResponse {
	private String month;
	private int y2023;
	private int y2024;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public int getY2023() {
		return y2023;
	}

	public void setY2023(int y2023) {
		this.y2023 = y2023;
	}

	public int getY2024() {
		return y2024;
	}

	public void setY2024(int y2024) {
		this.y2024 = y2024;
	}
}
