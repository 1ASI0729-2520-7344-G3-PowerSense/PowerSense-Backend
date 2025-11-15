package com.powersense.inventory.scheduling.application.internal.queryservices;

import com.powersense.inventory.scheduling.application.internal.outboundservices.repositories.ScheduleRepository;
import com.powersense.inventory.scheduling.domain.model.queries.GetScheduleStats;
import com.powersense.inventory.scheduling.domain.model.valueobjects.ScheduleStats;
import org.springframework.stereotype.Service;

@Service
public class ScheduleStatsQueryServiceImpl {

	private final ScheduleRepository scheduleRepository;

	public ScheduleStatsQueryServiceImpl(ScheduleRepository scheduleRepository) {
		this.scheduleRepository = scheduleRepository;
	}

	public ScheduleStats getScheduleStats(GetScheduleStats query) {
		var enabled = scheduleRepository.findAll().stream().filter(s -> s.isEnabled()).toList();
		int total = enabled.size();
		int activeHours = enabled.stream().mapToInt(s -> s.getEntries().size()).sum(); // aproximación simple
		double estimatedSavings = 0.0; // placeholder al no tener watts en scheduling
		return new ScheduleStats(total, activeHours, estimatedSavings);
	}
}
