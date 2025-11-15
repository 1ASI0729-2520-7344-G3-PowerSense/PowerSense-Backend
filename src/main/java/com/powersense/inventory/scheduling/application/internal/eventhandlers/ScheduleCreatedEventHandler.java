package com.powersense.inventory.scheduling.application.internal.eventhandlers;

import com.powersense.inventory.scheduling.domain.model.events.ScheduleCreated;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ScheduleCreatedEventHandler {

	@EventListener
	public void handle(ScheduleCreated event) {
		System.out.println("[ScheduleCreatedEventHandler] Schedule " + event.scheduleId() +
				" created for device " + event.deviceId());
	}
}
