package com.powersense.inventory.scheduling.domain.model.commands;

import java.util.List;
import java.util.Map;

public record UpdateScheduleRule(
		String ruleId,
		Boolean enabled,
		Integer priority,
		List<RuleConditionData> conditions,
		List<RuleActionData> actions
) {
	public record RuleConditionData(String type, Map<String, Object> value) {}

	public record RuleActionData(
			String type,
			List<String> deviceIds,
			List<String> roomIds,
			List<String> excludeDeviceIds,
			List<String> excludeRoomIds,
			String category,
			Object value
	) {}
}
