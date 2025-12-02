package com.powersense.inventory.devices.infrastructure.events;

import com.powersense.inventory.devices.application.internal.eventbus.EventBus;
import com.powersense.inventory.devices.domain.model.events.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component("devicesEventBus")
public class SpringEventBus implements EventBus {

    // Publicador de eventos propio de Spring
    private final ApplicationEventPublisher publisher;

    public SpringEventBus(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(DomainEvent event) {
        // Publica un solo evento de dominio
        publisher.publishEvent(event);
    }

    @Override
    public void publish(Iterable<? extends DomainEvent> events) {
        // Publica varios eventos uno por uno
        for (DomainEvent e : events) {
            publisher.publishEvent(e);
        }
    }
}
