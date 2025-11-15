package com.powersense.analytics.dashboard.application.model;

import java.time.Instant;

public class DashboardAlertResponse {
	private String id;
	private String severity;
	private String icon;
	private String message;
	private Instant timestamp;

	public DashboardAlertResponse(String id, String severity, String icon, String message, Instant timestamp) {
		this.id = id;
		this.severity = severity;
		this.icon = icon;
		this.message = message;
		this.timestamp = timestamp;
	}

	public String getId() {
		return id;
	}

	public String getSeverity() {
		return severity;
	}

	public String getIcon() {
		return icon;
	}

	public String getMessage() {
		return message;
	}

	public Instant getTimestamp() {
		return timestamp;
	}
}


