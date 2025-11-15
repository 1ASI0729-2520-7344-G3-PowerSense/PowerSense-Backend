package com.powersense.inventory.scheduling.application.internal.eventbus;

import com.powersense.inventory.scheduling.domain.model.events.DomainEvent;

public interface EventBus {
	void publish(DomainEvent event);
	void publish(Iterable<? extends DomainEvent> events);
}


