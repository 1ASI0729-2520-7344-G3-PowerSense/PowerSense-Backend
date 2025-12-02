package com.powersense.inventory.devices.infrastructure.persistence.jpa.adapters;

import com.powersense.inventory.devices.application.internal.outboundservices.repositories.DeviceRepository;
import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceId;
import com.powersense.inventory.devices.domain.model.valueobjects.RoomId;
import com.powersense.inventory.devices.infrastructure.persistence.jpa.entities.DeviceEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class DeviceRepositoryAdapter implements DeviceRepository {

	private final com.powersense.inventory.devices.infrastructure.persistence.jpa.repositories.DeviceRepository jpaRepository;

	public DeviceRepositoryAdapter(
			com.powersense.inventory.devices.infrastructure.persistence.jpa.repositories.DeviceRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public Optional<Device> findById(DeviceId id) {
		return jpaRepository.findById(id.value()).map(DeviceEntity::toDomain);
	}

	@Override
	public List<Device> findAll() {
		return jpaRepository.findAll().stream().map(DeviceEntity::toDomain).collect(Collectors.toList());
	}

	@Override
	public List<Device> findByRoomId(RoomId roomId) {
		return jpaRepository.findByLocationRoomId(roomId.value()).stream().map(DeviceEntity::toDomain)
				.collect(Collectors.toList());
	}

	@Override
	public Device save(Device device) {
		return jpaRepository.save(DeviceEntity.fromDomain(device)).toDomain();
	}

	@Override
	public void saveAll(List<Device> devices) {
		List<DeviceEntity> entities = devices.stream().map(DeviceEntity::fromDomain).collect(Collectors.toList());
		jpaRepository.saveAll(entities);
	}

	@Override
	public void deleteById(DeviceId id) {
		jpaRepository.deleteById(id.value());
	}

	@Override
	public DeviceId nextIdentity() {
		return new DeviceId("dev-" + UUID.randomUUID());
	}

	@Override
	public List<Device> findAllByUserId(Long userId) {
		return jpaRepository.findAllByUserId(userId).stream()
				.map(DeviceEntity::toDomain)
				.collect(Collectors.toList());
	}
}
