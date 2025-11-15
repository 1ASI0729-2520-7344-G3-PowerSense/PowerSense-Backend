package com.powersense.inventory.scheduling.infrastructure.events;

import com.powersense.inventory.scheduling.application.internal.eventbus.EventBus;
import com.powersense.inventory.scheduling.domain.model.events.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component("schedulingEventBus")
public class SpringEventBus implements EventBus {

	private final ApplicationEventPublisher publisher;

	public SpringEventBus(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	@Override
	public void publish(DomainEvent event) {
		publisher.publishEvent(event);
	}

	@Override
	public void publish(Iterable<? extends DomainEvent> events) {
		for (DomainEvent e : events) {
			publisher.publishEvent(e);
		}
	}
}


