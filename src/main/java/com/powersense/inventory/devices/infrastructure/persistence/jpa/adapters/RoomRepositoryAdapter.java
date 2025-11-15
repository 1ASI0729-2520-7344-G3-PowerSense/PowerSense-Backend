//package com.powersense.inventory.devices.infrastructure.persistence.jpa.adapters;
//
//import com.powersense.inventory.devices.application.internal.outboundservices.repositories.RoomRepository;
//import com.powersense.inventory.devices.domain.model.aggregates.Room;
//import com.powersense.inventory.devices.domain.model.valueobjects.RoomId;
//import com.powersense.inventory.devices.infrastructure.persistence.jpa.entities.RoomEntity;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Repository
//public class RoomRepositoryAdapter implements RoomRepository {
//
//	private final com.powersense.inventory.devices.infrastructure.persistence.jpa.repositories.RoomRepository jpaRepository;
//
//	public RoomRepositoryAdapter(com.powersense.inventory.devices.infrastructure.persistence.jpa.repositories.RoomRepository jpaRepository) {
//		this.jpaRepository = jpaRepository;
//	}
//
//	@Override
//	public Optional<Room> findById(RoomId id) {
//		return jpaRepository.findById(id.value()).map(RoomEntity::toDomain);
//	}
//
//	@Override
//	public List<Room> findAll() {
//		return jpaRepository.findAll().stream().map(RoomEntity::toDomain).collect(Collectors.toList());
//	}
//}


