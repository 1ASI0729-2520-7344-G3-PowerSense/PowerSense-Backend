package com.powersense.analytics.dashboard.application.model;

public class DashboardTipResponse {
	private String id;
	private String message;
	private String category;

	public DashboardTipResponse(String id, String message, String category) {
		this.id = id;
		this.message = message;
		this.category = category;
	}

	public String getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public String getCategory() {
		return category;
	}
}


