package com.powersense.analytics.reports.interfaces.rest.resources;

public class ReportHistoryResponse {
	private String id;
	private String date;
	private String name;
	private String type;
	private String department;
	private double consumptionKWh;
	private double cost;
	private int variationPct;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public double getConsumptionKWh() {
		return consumptionKWh;
	}

	public void setConsumptionKWh(double consumptionKWh) {
		this.consumptionKWh = consumptionKWh;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public int getVariationPct() {
		return variationPct;
	}

	public void setVariationPct(int variationPct) {
		this.variationPct = variationPct;
	}
}
