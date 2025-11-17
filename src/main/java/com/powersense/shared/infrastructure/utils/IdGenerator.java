package com.powersense.shared.infrastructure.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdGenerator {

	public String generateDeviceId() {
		return "dev-" + UUID.randomUUID().toString().substring(0, 8);
	}

	public String generateScheduleId() {
		return "sch-" + UUID.randomUUID().toString().substring(0, 8);
	}

	public String generateRuleId() {
		return "rule-" + UUID.randomUUID().toString().substring(0, 8);
	}

	public String generateReportId() {
		return "hist-" + UUID.randomUUID().toString().substring(0, 8);
	}

	public String generateUUID() {
		return UUID.randomUUID().toString();
	}

	public String generateAlertId() {
		return "alert-" + UUID.randomUUID().toString().substring(0, 8);
	}
}
