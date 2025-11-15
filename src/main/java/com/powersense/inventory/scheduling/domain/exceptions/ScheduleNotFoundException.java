package com.powersense.inventory.scheduling.domain.exceptions;

public class ScheduleNotFoundException extends DomainException {
    public ScheduleNotFoundException(String scheduleId) {
        super("Schedule not found: " + scheduleId);
    }
}
