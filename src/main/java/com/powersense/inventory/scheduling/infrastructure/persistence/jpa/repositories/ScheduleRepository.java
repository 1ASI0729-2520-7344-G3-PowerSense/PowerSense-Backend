package com.powersense.inventory.scheduling.infrastructure.persistence.jpa.repositories;

import com.powersense.inventory.scheduling.infrastructure.persistence.jpa.entities.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, String> {
	Optional<ScheduleEntity> findByDeviceId(String deviceId);

	List<ScheduleEntity> findByEnabled(boolean enabled);

	List<ScheduleEntity> findByRoomName(String roomName);

	List<ScheduleEntity> findByDeviceNameContainingIgnoreCase(String search);

	boolean existsByDeviceId(String deviceId);

	List<ScheduleEntity> findAllByUserId(Long userId);
}
