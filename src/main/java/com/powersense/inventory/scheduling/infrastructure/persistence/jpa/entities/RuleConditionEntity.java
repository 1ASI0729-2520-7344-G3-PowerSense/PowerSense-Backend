package com.powersense.inventory.scheduling.infrastructure.persistence.jpa.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.powersense.inventory.scheduling.domain.model.entities.RuleCondition;
import com.powersense.inventory.scheduling.domain.model.valueobjects.ConditionType;
import jakarta.persistence.*;

@Entity
@Table(name = "rule_conditions")
public class RuleConditionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rule_id", nullable = false)
	private ScheduleRuleEntity rule;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ConditionType type;

	@Column(columnDefinition = "JSON")
	private String valueJson;

	public static RuleConditionEntity fromDomain(RuleCondition condition) {
		RuleConditionEntity e = new RuleConditionEntity();
		e.type = condition.getType();
		try {
			e.valueJson = new ObjectMapper().writeValueAsString(condition.getValue());
		} catch (JsonProcessingException ex) {
			throw new IllegalArgumentException("Cannot serialize condition value to JSON", ex);
		}
		return e;
	}

	public RuleCondition toDomain() {
		try {
			Object value = new ObjectMapper().readValue(valueJson, Object.class);
			return new RuleCondition(type, value);
		} catch (Exception ex) {
			throw new IllegalArgumentException("Cannot deserialize condition value from JSON", ex);
		}
	}

	public void setRule(ScheduleRuleEntity rule) {
		this.rule = rule;
	}
}


