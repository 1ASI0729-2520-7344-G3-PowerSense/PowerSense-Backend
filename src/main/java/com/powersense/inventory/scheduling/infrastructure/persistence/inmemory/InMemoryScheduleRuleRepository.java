package com.powersense.inventory.scheduling.infrastructure.persistence.inmemory;

import com.powersense.inventory.scheduling.application.internal.outboundservices.repositories.ScheduleRuleRepository;
import com.powersense.inventory.scheduling.domain.model.aggregates.ScheduleRule;
import com.powersense.inventory.scheduling.domain.model.valueobjects.RuleId;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryScheduleRuleRepository implements ScheduleRuleRepository {

    private final Map<RuleId, ScheduleRule> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<ScheduleRule> findById(RuleId id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<ScheduleRule> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public ScheduleRule save(ScheduleRule rule) {
        storage.put(rule.getId(), rule);
        return rule;
    }
}


