package com.powersense.analytics.reports.application.model;

public class DepartmentMetricResponse {
	private String roomId;
	private String roomName;
	private String metricType;
	private double value;

	public DepartmentMetricResponse(String roomId, String roomName, String metricType, double value) {
		this.roomId = roomId;
		this.roomName = roomName;
		this.metricType = metricType;
		this.value = value;
	}

	public String getRoomId() {
		return roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public String getMetricType() {
		return metricType;
	}

	public double getValue() {
		return value;
	}
}


