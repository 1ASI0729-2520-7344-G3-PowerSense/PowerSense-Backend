package com.powersense.shared.infrastructure.messaging;

import com.powersense.shared.domain.model.base.DomainEvent;

public interface EventBus {
	void publish(DomainEvent event);
	void publish(Iterable<? extends DomainEvent> events);
}
