package com.powersense.inventory.scheduling.application.internal.outboundservices.repositories;

import com.powersense.inventory.scheduling.domain.model.aggregates.QuickSchedule;
import com.powersense.inventory.scheduling.domain.model.valueobjects.QuickScheduleId;

import java.util.Optional;

public interface QuickScheduleRepository {
	Optional<QuickSchedule> findById(QuickScheduleId id);
}


