package com.powersense.inventory.devices.infrastructure.persistence.jpa.repositories;

import com.powersense.inventory.devices.infrastructure.persistence.jpa.entities.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<DeviceEntity, String> {
	List<DeviceEntity> findByLocationRoomId(String roomId);

	List<DeviceEntity> findByStatus(String status);

	List<DeviceEntity> findByCategory(String category);

	List<DeviceEntity> findByNameContainingIgnoreCaseOrLocationRoomNameContainingIgnoreCase(String name,
			String roomName);

	Optional<DeviceEntity> findById(String id);

	List<DeviceEntity> findAllByUserId(Long userId);
}
