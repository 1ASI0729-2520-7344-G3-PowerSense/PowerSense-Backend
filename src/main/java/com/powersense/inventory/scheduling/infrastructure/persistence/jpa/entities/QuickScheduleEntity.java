package com.powersense.inventory.scheduling.infrastructure.persistence.jpa.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.powersense.inventory.scheduling.domain.model.aggregates.QuickSchedule;
import com.powersense.inventory.scheduling.domain.model.entities.ScheduleEntry;
import com.powersense.inventory.scheduling.domain.model.valueobjects.DayOfWeek;
import com.powersense.inventory.scheduling.domain.model.valueobjects.QuickScheduleId;
import com.powersense.inventory.scheduling.domain.model.valueobjects.QuickScheduleScope;
import com.powersense.inventory.scheduling.domain.model.valueobjects.ScheduleAction;
import com.powersense.inventory.scheduling.domain.model.valueobjects.TimeSlot;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "quick_schedules")
public class QuickScheduleEntity {
	@Id
	private String id;

	@Column(nullable = false)
	private String name;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private QuickScheduleScope scope;

	@Column(nullable = false)
	private String icon;

	@ElementCollection
	@CollectionTable(name = "quick_schedule_rooms", joinColumns = @JoinColumn(name = "quick_schedule_id"))
	@Column(name = "room_id")
	private List<String> roomIds = new ArrayList<>();

	@Column(columnDefinition = "JSON")
	private String defaultSchedulesJson;

	public static QuickScheduleEntity fromDomain(QuickSchedule quickSchedule) {
		QuickScheduleEntity e = new QuickScheduleEntity();
		e.id = quickSchedule.getId().value();
		e.name = quickSchedule.getName();
		e.description = quickSchedule.getDescription();
		e.scope = quickSchedule.getScope();
		e.icon = quickSchedule.getIcon();
		e.roomIds = new ArrayList<>(quickSchedule.getApplicableRoomIds());
		try {
			var dtoList = quickSchedule.getDefaultEntries().stream().map(entry -> Map.of(
					"action", entry.getAction().toString(),
					"hour", entry.getTime().hour(),
					"minute", entry.getTime().minute(),
					"days", entry.getDays().stream().map(Enum::name).collect(Collectors.toList())
			)).toList();
			e.defaultSchedulesJson = new ObjectMapper().writeValueAsString(dtoList);
		} catch (JsonProcessingException ex) {
			throw new IllegalArgumentException("Cannot serialize default entries", ex);
		}
		return e;
	}

	@SuppressWarnings("unchecked")
	public QuickSchedule toDomain() {
		try {
			List<Map<String, Object>> dtoList = new ObjectMapper().readValue(defaultSchedulesJson, List.class);
			List<ScheduleEntry> entries = dtoList.stream().map(m -> {
				ScheduleAction action = "on".equalsIgnoreCase((String) m.get("action")) ? ScheduleAction.ON : ScheduleAction.OFF;
				int hour = ((Number) m.get("hour")).intValue();
				int minute = ((Number) m.get("minute")).intValue();
				List<String> days = (List<String>) m.get("days");
				List<DayOfWeek> dayEnums = days.stream().map(DayOfWeek::valueOf).toList();
				return new ScheduleEntry(action, new TimeSlot(hour, minute), dayEnums);
			}).toList();
			return new QuickSchedule(new QuickScheduleId(id), name, description, scope, icon, roomIds, entries);
		} catch (Exception ex) {
			throw new IllegalArgumentException("Cannot deserialize default schedules", ex);
		}
	}
}


