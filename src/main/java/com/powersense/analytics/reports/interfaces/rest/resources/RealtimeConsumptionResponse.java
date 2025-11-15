package com.powersense.analytics.reports.interfaces.rest.resources;

public class RealtimeConsumptionResponse {
	private String period;
	private String name;
	private double value;

	public RealtimeConsumptionResponse() {
	}

	public RealtimeConsumptionResponse(String period, String name, double value) {
		this.period = period;
		this.name = name;
		this.value = value;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
