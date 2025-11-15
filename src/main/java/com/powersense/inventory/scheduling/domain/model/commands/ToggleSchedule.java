package com.powersense.inventory.scheduling.domain.model.commands;

public record ToggleSchedule(String scheduleId, boolean enabled) {}
