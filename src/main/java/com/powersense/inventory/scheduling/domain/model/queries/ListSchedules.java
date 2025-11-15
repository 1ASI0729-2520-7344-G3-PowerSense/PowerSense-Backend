package com.powersense.inventory.scheduling.domain.model.queries;

public record ListSchedules(
		String search,
		String roomId,
		Boolean enabled,
		String deviceId
) {
	public ListSchedules() {
		this(null, null, null, null);
	}
}
