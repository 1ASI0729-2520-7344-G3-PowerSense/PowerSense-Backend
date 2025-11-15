package com.powersense.inventory.scheduling.infrastructure.persistence.inmemory;

import com.powersense.inventory.scheduling.domain.model.aggregates.Schedule;
import com.powersense.inventory.scheduling.domain.repository.ScheduleRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@Repository

public class InMemoryScheduleRepository implements ScheduleRepository {

    private final Map<String, Schedule> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Schedule> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Schedule> findByDeviceId(String deviceId) {
        return store.values().stream()
                .filter(s -> s.getDeviceId().value().equals(deviceId))
                .findFirst();
    }

    @Override
    public List<Schedule> findByEnabled(boolean enabled) {
        return store.values().stream()
                .filter(s -> s.isEnabled() == enabled)
                .toList();
    }

    @Override
    public List<Schedule> findByRoomName(String roomName) {
        return store.values().stream()
                .filter(s -> s.getRoomName().equalsIgnoreCase(roomName))
                .toList();
    }

    @Override
    public List<Schedule> findByDeviceNameContaining(String search) {
        return store.values().stream()
                .filter(s -> s.getDeviceName().toLowerCase().contains(search.toLowerCase()))
                .toList();
    }

    @Override
    public boolean existsByDeviceId(String deviceId) {
        return store.values().stream()
                .anyMatch(s -> s.getDeviceId().value().equals(deviceId));
    }

    @Override
    public Schedule save(Schedule schedule) {
        store.put(schedule.getId().value(), schedule);
        return schedule;
    }

    @Override
    public List<Schedule> findAll() {
        return new ArrayList<>(store.values());
    }
}




