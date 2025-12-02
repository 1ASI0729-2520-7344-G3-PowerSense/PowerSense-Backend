package com.powersense.inventory.scheduling.application.internal.commandservices;

import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.devices.domain.model.valueobjects.DeviceId;
import com.powersense.inventory.scheduling.application.internal.eventbus.EventBus;
import com.powersense.inventory.scheduling.application.internal.outboundservices.acl.DeviceControlAdapter;
import com.powersense.inventory.scheduling.application.internal.outboundservices.repositories.QuickScheduleRepository;
import com.powersense.inventory.scheduling.application.internal.outboundservices.repositories.ScheduleRepository;
import com.powersense.inventory.scheduling.application.internal.outboundservices.repositories.ScheduleRuleRepository;
import com.powersense.inventory.scheduling.domain.exceptions.DuplicateScheduleException;
import com.powersense.inventory.scheduling.domain.exceptions.RuleNotFoundException;
import com.powersense.inventory.scheduling.domain.exceptions.ScheduleNotFoundException;
import com.powersense.inventory.scheduling.domain.model.aggregates.QuickSchedule;
import com.powersense.inventory.scheduling.domain.model.aggregates.Schedule;
import com.powersense.inventory.scheduling.domain.model.aggregates.ScheduleRule;
import com.powersense.inventory.scheduling.domain.model.commands.*;
import com.powersense.inventory.scheduling.domain.model.entities.RuleAction;
import com.powersense.inventory.scheduling.domain.model.entities.RuleCondition;
import com.powersense.inventory.scheduling.domain.model.entities.ScheduleEntry;
import com.powersense.inventory.scheduling.domain.model.events.QuickScheduleApplied;
import com.powersense.inventory.scheduling.domain.model.events.ScheduleDeleted;
import com.powersense.inventory.scheduling.domain.model.valueobjects.*;
import com.powersense.inventory.scheduling.domain.services.ScheduleService;
import com.powersense.auth.infrastructure.tokens.JwtTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleCommandServiceImpl {

	private final ScheduleRepository scheduleRepository;
	private final ScheduleRuleRepository scheduleRuleRepository;
	private final QuickScheduleRepository quickScheduleRepository;
	private final ScheduleService scheduleService;
	private final EventBus eventBus;
	private final DeviceControlAdapter deviceControl;
	private final JwtTokenService jwtTokenService;

	public ScheduleCommandServiceImpl(ScheduleRepository scheduleRepository,
			ScheduleRuleRepository scheduleRuleRepository,
			QuickScheduleRepository quickScheduleRepository,
			ScheduleService scheduleService,
			EventBus eventBus,
			DeviceControlAdapter deviceControl,
			JwtTokenService jwtTokenService) {
		this.scheduleRepository = scheduleRepository;
		this.scheduleRuleRepository = scheduleRuleRepository;
		this.quickScheduleRepository = quickScheduleRepository;
		this.scheduleService = scheduleService;
		this.eventBus = eventBus;
		this.deviceControl = deviceControl;
		this.jwtTokenService = jwtTokenService;
	}

	public Schedule createSchedule(CreateSchedule command) {
		Long userId = getCurrentUserId();
		DeviceId deviceId = new DeviceId(command.deviceId());
		Optional<Schedule> existing = scheduleRepository.findByDeviceId(deviceId);
		if (existing.isPresent()) {
			throw new DuplicateScheduleException(deviceId.value());
		}

		List<ScheduleEntry> entries = toEntries(command.schedules());
		ScheduleId id = scheduleRepository.nextIdentity();
		Schedule schedule = new Schedule(id, deviceId, command.deviceName(), command.roomName(), command.enabled(),
				entries, userId);

		scheduleService.validateTimeSlotCoherence(entries);
		schedule.validateNoTimeConflicts();

		Schedule saved = scheduleRepository.save(schedule);
		eventBus.publish(saved.pullDomainEvents());
		return saved;
	}

	public Schedule updateSchedule(UpdateSchedule command) {
		ScheduleId id = new ScheduleId(command.scheduleId());
		Schedule schedule = scheduleRepository.findById(id)
				.orElseThrow(() -> new ScheduleNotFoundException(id.value()));

		if (command.schedules() != null) {

			List<ScheduleEntry> current = new ArrayList<>(schedule.getEntries());
			for (int i = current.size() - 1; i >= 0; i--) {
				schedule.removeEntry(i);
			}
			List<ScheduleEntry> newEntries = toEntries(command.schedules());
			for (ScheduleEntry e : newEntries) {
				schedule.addEntry(e);
			}
			scheduleService.validateTimeSlotCoherence(newEntries);
			schedule.validateNoTimeConflicts();
		}

		if (command.enabled() != null) {
			if (command.enabled())
				schedule.enable();
			else
				schedule.disable();
		}

		Schedule saved = scheduleRepository.save(schedule);
		eventBus.publish(saved.pullDomainEvents());
		return saved;
	}

	public void deleteSchedule(DeleteSchedule command) {
		ScheduleId id = new ScheduleId(command.scheduleId());
		scheduleRepository.deleteById(id);
		eventBus.publish(new ScheduleDeleted(id.value(), Instant.now()));
	}

	public Schedule toggleSchedule(ToggleSchedule command) {
		ScheduleId id = new ScheduleId(command.scheduleId());
		Schedule schedule = scheduleRepository.findById(id)
				.orElseThrow(() -> new ScheduleNotFoundException(id.value()));

		if (command.enabled())
			schedule.enable();
		else
			schedule.disable();

		Schedule saved = scheduleRepository.save(schedule);
		eventBus.publish(saved.pullDomainEvents());
		return saved;
	}

	public ScheduleRule toggleScheduleRule(ToggleScheduleRule command) {
		RuleId id = new RuleId(command.ruleId());
		ScheduleRule rule = scheduleRuleRepository.findById(id)
				.orElseThrow(() -> new RuleNotFoundException(id.value()));

		if (command.enabled())
			rule.enable();
		else
			rule.disable();

		ScheduleRule saved = scheduleRuleRepository.save(rule);
		eventBus.publish(saved.pullDomainEvents());
		return saved;
	}

	public List<Schedule> applyQuickSchedule(ApplyQuickSchedule command) {
		QuickScheduleId presetId = new QuickScheduleId(command.presetId());
		QuickSchedule preset = quickScheduleRepository.findById(presetId)
				.orElseThrow(() -> new IllegalArgumentException("QuickSchedule not found: " + presetId.value()));

		List<Device> allDevices = deviceControl.getAllDevices();
		List<Device> targetDevices = allDevices.stream()
				.filter(preset::appliesTo)
				.collect(Collectors.toList());

		List<Schedule> affected = new ArrayList<>();
		for (Device d : targetDevices) {
			DeviceId deviceId = d.getId();
			Optional<Schedule> existing = scheduleRepository.findByDeviceId(deviceId);
			if (existing.isPresent()) {
				Schedule schedule = existing.get();

				List<ScheduleEntry> current = new ArrayList<>(schedule.getEntries());
				for (int i = current.size() - 1; i >= 0; i--) {
					schedule.removeEntry(i);
				}
				for (ScheduleEntry e : preset.getDefaultEntries()) {
					schedule.addEntry(e);
				}
				schedule.enable();
				affected.add(scheduleRepository.save(schedule));
				eventBus.publish(schedule.pullDomainEvents());
			} else {
				Long userId = getCurrentUserId();
				ScheduleId id = scheduleRepository.nextIdentity();
				Schedule schedule = new Schedule(id, deviceId, d.getName().value(), d.getLocation().roomName().value(),
						true, preset.getDefaultEntries(), userId);
				affected.add(scheduleRepository.save(schedule));
				eventBus.publish(schedule.pullDomainEvents());
			}
		}

		eventBus.publish(new QuickScheduleApplied(presetId.value(), presetId.value(), affected.size(), Instant.now()));
		return affected;
	}

	public ScheduleRule updateScheduleRule(UpdateScheduleRule command) {
		RuleId id = new RuleId(command.ruleId());
		ScheduleRule rule = scheduleRuleRepository.findById(id)
				.orElseThrow(() -> new RuleNotFoundException(id.value()));

		if (command.enabled() != null) {
			if (command.enabled())
				rule.enable();
			else
				rule.disable();
		}
		if (command.priority() != null) {
			rule.setPriority(command.priority());
		}
		if (command.conditions() != null) {
			for (UpdateScheduleRule.RuleConditionData c : command.conditions()) {
				rule.addCondition(new RuleCondition(parseConditionType(c.type()), c.value()));
			}
		}
		if (command.actions() != null) {
			for (UpdateScheduleRule.RuleActionData a : command.actions()) {
				rule.addAction(new RuleAction(parseActionType(a.type()), a.deviceIds(), a.roomIds(),
						a.excludeDeviceIds(), a.excludeRoomIds(), a.category(), a.value()));
			}
		}

		ScheduleRule saved = scheduleRuleRepository.save(rule);
		eventBus.publish(saved.pullDomainEvents());
		return saved;
	}

	private List<ScheduleEntry> toEntries(List<CreateSchedule.ScheduleEntryData> data) {
		if (data == null)
			return List.of();
		List<ScheduleEntry> entries = new ArrayList<>();
		for (CreateSchedule.ScheduleEntryData d : data) {
			ScheduleAction action = parseAction(d.action());
			TimeSlot time = new TimeSlot(d.time().hour(), d.time().minute());
			List<DayOfWeek> days = d.days().stream().map(DayOfWeek::fromString).toList();
			entries.add(new ScheduleEntry(action, time, days));
		}
		return entries;
	}

	private ScheduleAction parseAction(String raw) {
		if (raw == null)
			return ScheduleAction.OFF;
		return "on".equalsIgnoreCase(raw) ? ScheduleAction.ON : ScheduleAction.OFF;
	}

	private ConditionType parseConditionType(String raw) {
		String v = raw == null ? "" : raw.trim().toLowerCase(Locale.ROOT);
		for (ConditionType t : ConditionType.values()) {
			if (t.name().equalsIgnoreCase(v) || t.toString().equalsIgnoreCase(v))
				return t;
		}
		throw new IllegalArgumentException("Unknown condition type: " + raw);
	}

	private ActionType parseActionType(String raw) {
		String v = raw == null ? "" : raw.trim().toLowerCase(Locale.ROOT);
		for (ActionType t : ActionType.values()) {
			if (t.name().equalsIgnoreCase(v) || t.toString().equalsIgnoreCase(v))
				return t;
		}
		throw new IllegalArgumentException("Unknown action type: " + raw);
	}

	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}
		String token = (String) authentication.getCredentials();
		if (token == null) {
			return null;
		}
		return jwtTokenService.extractUserId(token);
	}
}
