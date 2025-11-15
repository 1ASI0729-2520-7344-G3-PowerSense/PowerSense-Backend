package com.powersense.inventory.scheduling.application.internal.queryservices;

import com.powersense.inventory.devices.domain.model.valueobjects.DeviceId;
import com.powersense.inventory.scheduling.application.internal.outboundservices.repositories.ScheduleRepository;
import com.powersense.inventory.scheduling.application.internal.outboundservices.repositories.ScheduleRuleRepository;
import com.powersense.inventory.scheduling.domain.exceptions.RuleNotFoundException;
import com.powersense.inventory.scheduling.domain.exceptions.ScheduleNotFoundException;
import com.powersense.inventory.scheduling.domain.model.aggregates.Schedule;
import com.powersense.inventory.scheduling.domain.model.aggregates.ScheduleRule;
import com.powersense.inventory.scheduling.domain.model.queries.*;
import com.powersense.inventory.scheduling.domain.model.valueobjects.RuleId;
import com.powersense.inventory.scheduling.domain.model.valueobjects.ScheduleId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class ScheduleQueryServiceImpl {

	private final ScheduleRepository scheduleRepository;
	private final ScheduleRuleRepository scheduleRuleRepository;

	public ScheduleQueryServiceImpl(ScheduleRepository scheduleRepository,
									ScheduleRuleRepository scheduleRuleRepository) {
		this.scheduleRepository = scheduleRepository;
		this.scheduleRuleRepository = scheduleRuleRepository;
	}

	public List<Schedule> listSchedules(ListSchedules query) {
		List<Schedule> all = scheduleRepository.findAll();
		if (query == null) return all;
		String search = normalize(query.search());
		String roomId = normalize(query.roomId());
		Boolean enabled = query.enabled();
		String deviceId = normalize(query.deviceId());

		return all.stream()
				.filter(s -> search == null || s.getDeviceName().toLowerCase(Locale.ROOT).contains(search))
				.filter(s -> roomId == null || s.getRoomName().equalsIgnoreCase(roomId))
				.filter(s -> enabled == null || s.isEnabled() == enabled)
				.filter(s -> deviceId == null || s.getDeviceId().value().equalsIgnoreCase(deviceId))
				.collect(Collectors.toList());
	}

	public Schedule getScheduleById(GetScheduleById query) {
		return scheduleRepository.findById(new ScheduleId(query.scheduleId()))
				.orElseThrow(() -> new ScheduleNotFoundException(query.scheduleId()));
	}

	public Schedule getScheduleByDeviceId(GetScheduleByDeviceId query) {
		return scheduleRepository.findByDeviceId(new DeviceId(query.deviceId()))
				.orElseThrow(() -> new ScheduleNotFoundException("deviceId=" + query.deviceId()));
	}

	public List<ScheduleRule> listScheduleRules(ListScheduleRules query) {
		return scheduleRuleRepository.findAll();
	}

	public ScheduleRule getScheduleRuleById(GetScheduleRuleById query) {
		return scheduleRuleRepository.findById(new RuleId(query.ruleId()))
				.orElseThrow(() -> new RuleNotFoundException(query.ruleId()));
	}

	private String normalize(String s) {
		if (s == null) return null;
		String t = s.trim();
		return t.isEmpty() ? null : t.toLowerCase(Locale.ROOT);
	}
}
