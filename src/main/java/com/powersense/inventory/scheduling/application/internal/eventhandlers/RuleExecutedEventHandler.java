package com.powersense.inventory.scheduling.application.internal.eventhandlers;

import com.powersense.inventory.scheduling.domain.model.events.RuleExecuted;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RuleExecutedEventHandler {

	@EventListener
	public void handle(RuleExecuted event) {
		System.out.println("[RuleExecutedEventHandler] Rule " + event.ruleId() +
				" (" + event.ruleName() + ") affected " + event.affectedDevices() + " devices");
	}
}
