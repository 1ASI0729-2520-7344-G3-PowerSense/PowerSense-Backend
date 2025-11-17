package com.powersense.inventory.devices.application.internal.eventbus;

import com.powersense.inventory.devices.domain.model.events.DomainEvent;

// EventBus define el contrato para la publicación de eventos de dominio dentro de la capa de aplicación.
// Este patrón permite desacoplar la generación de eventos (en el dominio) de su manejo (listeners, mensajería, etc.).
public interface EventBus {

    // Publica un único evento de dominio.
    // Se utiliza cuando una operación genera un solo evento específico.
    void publish(DomainEvent event);

    // Publica múltiples eventos de dominio.
    // Ideal para operaciones en lote (como actualización de múltiples dispositivos o importación masiva).
    void publish(Iterable<? extends DomainEvent> events);
}
