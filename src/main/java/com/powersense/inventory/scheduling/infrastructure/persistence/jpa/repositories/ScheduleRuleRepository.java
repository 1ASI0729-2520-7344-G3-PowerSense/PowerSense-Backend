package com.powersense.inventory.scheduling.infrastructure.persistence.jpa.repositories;

import com.powersense.inventory.scheduling.infrastructure.persistence.jpa.entities.ScheduleRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRuleRepository extends JpaRepository<ScheduleRuleEntity, String> {
	List<ScheduleRuleEntity> findByEnabledOrderByPriorityAsc(boolean enabled);
	List<ScheduleRuleEntity> findByType(String type);
	Optional<ScheduleRuleEntity> findByPriority(int priority);
}
