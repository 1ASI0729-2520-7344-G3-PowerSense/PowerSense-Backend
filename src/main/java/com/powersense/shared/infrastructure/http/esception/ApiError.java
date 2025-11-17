package com.powersense.shared.infrastructure.http.esception;

import java.time.Instant;

public record ApiError(
		String code,
		String message,
		int status,
		Instant timestamp
) {
	public String getTimestampISO() {
		return timestamp != null ? timestamp.toString() : null;
	}
}
