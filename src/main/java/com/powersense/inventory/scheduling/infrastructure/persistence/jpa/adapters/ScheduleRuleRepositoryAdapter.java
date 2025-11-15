//package com.powersense.inventory.scheduling.infrastructure.persistence.jpa.adapters;
//
//import com.powersense.inventory.scheduling.application.internal.outboundservices.repositories.ScheduleRuleRepository;
//import com.powersense.inventory.scheduling.domain.model.aggregates.ScheduleRule;
//import com.powersense.inventory.scheduling.domain.model.valueobjects.RuleId;
//import com.powersense.inventory.scheduling.infrastructure.persistence.jpa.entities.ScheduleRuleEntity;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Repository
//public class ScheduleRuleRepositoryAdapter implements ScheduleRuleRepository {
//
//	private final com.powersense.inventory.scheduling.infrastructure.persistence.jpa.repositories.ScheduleRuleRepository jpaRepository;
//
//	public ScheduleRuleRepositoryAdapter(com.powersense.inventory.scheduling.infrastructure.persistence.jpa.repositories.ScheduleRuleRepository jpaRepository) {
//		this.jpaRepository = jpaRepository;
//	}
//
//	@Override
//	public Optional<ScheduleRule> findById(RuleId id) {
//		return jpaRepository.findById(id.value()).map(ScheduleRuleEntity::toDomain);
//	}
//
//	@Override
//	public List<ScheduleRule> findAll() {
//		return jpaRepository.findAll().stream().map(ScheduleRuleEntity::toDomain).collect(Collectors.toList());
//	}
//
//	@Override
//	public ScheduleRule save(ScheduleRule rule) {
//		return jpaRepository.save(ScheduleRuleEntity.fromDomain(rule)).toDomain();
//	}
//}


