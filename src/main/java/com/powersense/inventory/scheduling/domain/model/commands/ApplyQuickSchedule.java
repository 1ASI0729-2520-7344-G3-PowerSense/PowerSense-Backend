package com.powersense.inventory.scheduling.domain.model.commands;

import java.util.List;

public record ApplyQuickSchedule(
		String presetId,
		String scope,
		List<String> customRoomIds
) {}
