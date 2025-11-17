package com.powersense.shared.infrastructure.messaging;

import com.powersense.shared.domain.model.base.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class InMemoryEventBus implements EventBus {

	private static final Logger logger = LoggerFactory.getLogger(InMemoryEventBus.class);

	private final ApplicationEventPublisher publisher;

	public InMemoryEventBus(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	@Override
	public void publish(DomainEvent event) {
		logger.info("Publishing event: {}", event.getClass().getSimpleName());
		publisher.publishEvent(event);
	}

	@Override
	public void publish(Iterable<? extends DomainEvent> events) {
		for (DomainEvent e : events) {
			publish(e);
		}
	}
}
