//package com.powersense.inventory.scheduling.infrastructure.persistence.jpa.adapters;
//
//import com.powersense.inventory.scheduling.application.internal.outboundservices.repositories.QuickScheduleRepository;
//import com.powersense.inventory.scheduling.domain.model.aggregates.QuickSchedule;
//import com.powersense.inventory.scheduling.domain.model.valueobjects.QuickScheduleId;
//import com.powersense.inventory.scheduling.infrastructure.persistence.jpa.entities.QuickScheduleEntity;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public class QuickScheduleRepositoryAdapter implements QuickScheduleRepository {
//
//	private final com.powersense.inventory.scheduling.infrastructure.persistence.jpa.repositories.QuickScheduleRepository jpaRepository;
//
//	public QuickScheduleRepositoryAdapter(com.powersense.inventory.scheduling.infrastructure.persistence.jpa.repositories.QuickScheduleRepository jpaRepository) {
//		this.jpaRepository = jpaRepository;
//	}
//
//	@Override
//	public Optional<QuickSchedule> findById(QuickScheduleId id) {
//		return jpaRepository.findById(id.value()).map(QuickScheduleEntity::toDomain);
//	}
//}


