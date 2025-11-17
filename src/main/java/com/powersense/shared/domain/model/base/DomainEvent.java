package com.powersense.shared.domain.model.base;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredOn();
}
