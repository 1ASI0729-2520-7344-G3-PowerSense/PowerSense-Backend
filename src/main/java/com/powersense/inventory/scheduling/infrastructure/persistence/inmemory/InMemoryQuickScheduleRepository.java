package com.powersense.inventory.scheduling.infrastructure.persistence.inmemory;

import com.powersense.inventory.scheduling.application.internal.outboundservices.repositories.QuickScheduleRepository;
import com.powersense.inventory.scheduling.domain.model.aggregates.QuickSchedule;
import com.powersense.inventory.scheduling.domain.model.valueobjects.QuickScheduleId;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryQuickScheduleRepository implements QuickScheduleRepository {

    private final Map<QuickScheduleId, QuickSchedule> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<QuickSchedule> findById(QuickScheduleId id) {
        return Optional.ofNullable(storage.get(id));
    }
}

