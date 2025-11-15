package com.powersense.inventory.scheduling.domain.model.aggregates;

import com.powersense.inventory.devices.domain.model.aggregates.Device;
import com.powersense.inventory.scheduling.domain.model.entities.ScheduleEntry;
import com.powersense.inventory.scheduling.domain.model.valueobjects.QuickScheduleId;
import com.powersense.inventory.scheduling.domain.model.valueobjects.QuickScheduleScope;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class QuickSchedule {
	private final QuickScheduleId id;
	private final String name;
	private final String description;
	private final QuickScheduleScope scope;
	private final String icon;
	private final List<String> roomIds;
	private final List<ScheduleEntry> defaultEntries;

	public QuickSchedule(QuickScheduleId id, String name, String description, QuickScheduleScope scope, String icon,
						 List<String> roomIds, List<ScheduleEntry> defaultEntries) {
		this.id = Objects.requireNonNull(id);
		this.name = Objects.requireNonNull(name);
		this.description = description;
		this.scope = Objects.requireNonNull(scope);
		this.icon = icon;
		this.roomIds = roomIds != null ? List.copyOf(roomIds) : List.of();
		this.defaultEntries = defaultEntries != null ? List.copyOf(defaultEntries) : List.of();
	}

	public List<String> getApplicableRoomIds() {
		return roomIds;
	}

	public boolean appliesTo(Device device) {
		return switch (scope) {
			case ALL -> true;
			case CUSTOM -> roomIds.contains(device.getLocation().roomId().value());
			case BEDROOMS -> isBedroom(device.getLocation().roomName().value());
			case COMMON_AREAS -> isCommonArea(device.getLocation().roomName().value());
		};
	}

	private boolean isBedroom(String roomName) {
		String r = roomName.toLowerCase(Locale.ROOT);
		return r.contains("bed") || r.contains("dorm") || r.contains("habit") || r.contains("bedroom");
	}

	private boolean isCommonArea(String roomName) {
		String r = roomName.toLowerCase(Locale.ROOT);
		return r.contains("living") || r.contains("sala") || r.contains("kitchen") || r.contains("cocina")
				|| r.contains("bath") || r.contains("baño") || r.contains("comedor");
	}

	public List<ScheduleEntry> getDefaultEntries() {
		return defaultEntries;
	}

	public QuickScheduleId getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public QuickScheduleScope getScope() {
		return scope;
	}

	public String getIcon() {
		return icon;
	}
}
