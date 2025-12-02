package com.powersense.inventory.scheduling.infrastructure.persistence.jpa.adapters;

import com.powersense.inventory.devices.domain.model.valueobjects.DeviceId;
import com.powersense.inventory.scheduling.application.internal.outboundservices.repositories.ScheduleRepository;
import com.powersense.inventory.scheduling.domain.model.aggregates.Schedule;
import com.powersense.inventory.scheduling.domain.model.valueobjects.ScheduleId;
import com.powersense.inventory.scheduling.infrastructure.persistence.jpa.entities.ScheduleEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ScheduleRepositoryAdapter implements ScheduleRepository {

	private final com.powersense.inventory.scheduling.infrastructure.persistence.jpa.repositories.ScheduleRepository jpaRepository;

	public ScheduleRepositoryAdapter(
			com.powersense.inventory.scheduling.infrastructure.persistence.jpa.repositories.ScheduleRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public Optional<Schedule> findById(ScheduleId id) {
		return jpaRepository.findById(id.value()).map(ScheduleEntity::toDomain);
	}

	@Override
	public Optional<Schedule> findByDeviceId(DeviceId deviceId) {
		return jpaRepository.findByDeviceId(deviceId.value()).map(ScheduleEntity::toDomain);
	}

	@Override
	public List<Schedule> findAll() {
		return jpaRepository.findAll().stream().map(ScheduleEntity::toDomain).collect(Collectors.toList());
	}

	@Override
	public Schedule save(Schedule schedule) {
		return jpaRepository.save(ScheduleEntity.fromDomain(schedule)).toDomain();
	}

	@Override
	public void saveAll(List<Schedule> schedules) {
		var entities = schedules.stream().map(ScheduleEntity::fromDomain).toList();
		jpaRepository.saveAll(entities);
	}

	@Override
	public void deleteById(ScheduleId id) {
		jpaRepository.deleteById(id.value());
	}

	@Override
	public ScheduleId nextIdentity() {
		return new ScheduleId("sch-" + UUID.randomUUID());
	}

	@Override
	public List<Schedule> findAllByUserId(Long userId) {
		return jpaRepository.findAllByUserId(userId).stream()
				.map(ScheduleEntity::toDomain)
				.collect(Collectors.toList());
	}
}
