package com.powersense.inventory.devices.application.internal.eventbus;

import com.powersense.inventory.devices.domain.model.events.DomainEvent;

public interface EventBus {
    void publish(DomainEvent event);
    void publish(Iterable<? extends DomainEvent> events);
}


