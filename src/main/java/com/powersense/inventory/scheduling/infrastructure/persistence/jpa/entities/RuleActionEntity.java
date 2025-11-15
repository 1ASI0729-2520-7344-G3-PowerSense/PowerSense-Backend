package com.powersense.inventory.scheduling.infrastructure.persistence.jpa.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.powersense.inventory.scheduling.domain.model.entities.RuleAction;
import com.powersense.inventory.scheduling.domain.model.valueobjects.ActionType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rule_actions")
public class RuleActionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rule_id", nullable = false)
	private ScheduleRuleEntity rule;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ActionType type;

	@ElementCollection
	@CollectionTable(name = "rule_action_device_ids", joinColumns = @JoinColumn(name = "action_id"))
	@Column(name = "device_id")
	private List<String> deviceIds = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "rule_action_room_ids", joinColumns = @JoinColumn(name = "action_id"))
	@Column(name = "room_id")
	private List<String> roomIds = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "rule_action_exclude_device_ids", joinColumns = @JoinColumn(name = "action_id"))
	@Column(name = "device_id")
	private List<String> excludeDeviceIds = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "rule_action_exclude_room_ids", joinColumns = @JoinColumn(name = "action_id"))
	@Column(name = "room_id")
	private List<String> excludeRoomIds = new ArrayList<>();

	@Column
	private String category;

	@Column(columnDefinition = "JSON")
	private String valueJson;

	public static RuleActionEntity fromDomain(RuleAction action) {
		RuleActionEntity e = new RuleActionEntity();
		e.type = action.getType();
		e.deviceIds = new ArrayList<>(action.getDeviceIds());
		e.roomIds = new ArrayList<>(action.getRoomIds());
		e.excludeDeviceIds = new ArrayList<>(action.getExcludeDeviceIds());
		e.excludeRoomIds = new ArrayList<>(action.getExcludeRoomIds());
		e.category = action.getCategory();
		try {
			e.valueJson = new ObjectMapper().writeValueAsString(action.getValue());
		} catch (JsonProcessingException ex) {
			throw new IllegalArgumentException("Cannot serialize action value to JSON", ex);
		}
		return e;
	}

	public RuleAction toDomain() {
		try {
			Object value = valueJson != null ? new ObjectMapper().readValue(valueJson, Object.class) : null;
			return new RuleAction(type, deviceIds, roomIds, excludeDeviceIds, excludeRoomIds, category, value);
		} catch (Exception ex) {
			throw new IllegalArgumentException("Cannot deserialize action value from JSON", ex);
		}
	}

	public void setRule(ScheduleRuleEntity rule) {
		this.rule = rule;
	}
}


