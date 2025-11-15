package com.powersense.inventory.scheduling.domain.model.commands;

public record ToggleScheduleRule(String ruleId, boolean enabled) {}
