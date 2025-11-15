package com.powersense.inventory.scheduling.infrastructure.persistence.jpa.entities;

import com.powersense.inventory.scheduling.domain.model.entities.ScheduleEntry;
import com.powersense.inventory.scheduling.domain.model.valueobjects.DayOfWeek;
import com.powersense.inventory.scheduling.domain.model.valueobjects.ScheduleAction;
import com.powersense.inventory.scheduling.domain.model.valueobjects.TimeSlot;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedule_entries")
public class ScheduleEntryEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schedule_id", nullable = false)
	private ScheduleEntity schedule;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ScheduleAction action;

	@Column(nullable = false)
	private int hour;

	@Column(nullable = false)
	private int minute;

	@ElementCollection
	@CollectionTable(name = "schedule_entry_days", joinColumns = @JoinColumn(name = "entry_id"))
	@Column(name = "day")
	@Enumerated(EnumType.STRING)
	private List<DayOfWeek> days = new ArrayList<>();

	public static ScheduleEntryEntity fromDomain(ScheduleEntry entry) {
		ScheduleEntryEntity e = new ScheduleEntryEntity();
		e.action = entry.getAction();
		e.hour = entry.getTime().hour();
		e.minute = entry.getTime().minute();
		e.days = new ArrayList<>(entry.getDays());
		return e;
	}

	public ScheduleEntry toDomain() {
		return new ScheduleEntry(
				action,
				new TimeSlot(hour, minute),
				List.copyOf(days)
		);
	}

	public void setSchedule(ScheduleEntity schedule) {
		this.schedule = schedule;
	}
}


