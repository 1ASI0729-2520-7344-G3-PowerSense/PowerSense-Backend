package com.powersense.inventory.scheduling.domain.repository;

import com.powersense.inventory.scheduling.domain.model.aggregates.Schedule;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {

    Optional<Schedule> findById(String id);

    Optional<Schedule> findByDeviceId(String deviceId);

    List<Schedule> findByEnabled(boolean enabled);

    List<Schedule> findByRoomName(String roomName);

    List<Schedule> findByDeviceNameContaining(String search);

    boolean existsByDeviceId(String deviceId);

    Schedule save(Schedule schedule);

    List<Schedule> findAll();
}

