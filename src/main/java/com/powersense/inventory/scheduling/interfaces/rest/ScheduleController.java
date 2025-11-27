package com.powersense.inventory.scheduling.interfaces.rest;

import com.powersense.inventory.scheduling.application.internal.commandservices.ScheduleCommandServiceImpl;
import com.powersense.inventory.scheduling.application.internal.queryservices.ScheduleQueryServiceImpl;
import com.powersense.inventory.scheduling.domain.model.aggregates.Schedule;
import com.powersense.inventory.scheduling.domain.model.commands.DeleteSchedule;
import com.powersense.inventory.scheduling.domain.model.commands.ToggleSchedule;
import com.powersense.inventory.scheduling.domain.model.queries.GetScheduleById;
import com.powersense.inventory.scheduling.domain.model.queries.ListSchedules;
import com.powersense.inventory.scheduling.interfaces.rest.resources.CreateScheduleResource;
import com.powersense.inventory.scheduling.interfaces.rest.resources.ScheduleResponse;
import com.powersense.inventory.scheduling.interfaces.rest.resources.ToggleScheduleResource;
import com.powersense.inventory.scheduling.interfaces.rest.resources.UpdateScheduleResource;
import com.powersense.inventory.scheduling.interfaces.rest.transform.ScheduleAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory/schedules")
@CrossOrigin
public class ScheduleController {

	private final ScheduleCommandServiceImpl scheduleCommandService;
	private final ScheduleQueryServiceImpl scheduleQueryService;
	private final ScheduleAssembler scheduleAssembler;

	public ScheduleController(ScheduleCommandServiceImpl scheduleCommandService,
							  ScheduleQueryServiceImpl scheduleQueryService,
							  ScheduleAssembler scheduleAssembler) {
		this.scheduleCommandService = scheduleCommandService;
		this.scheduleQueryService = scheduleQueryService;
		this.scheduleAssembler = scheduleAssembler;
	}

	@PostMapping
	public ResponseEntity<ScheduleResponse> createSchedule(@Valid @RequestBody CreateScheduleResource resource) {
		try {
			Schedule created = scheduleCommandService.createSchedule(scheduleAssembler.toCreateCommand(resource));
			return ResponseEntity.status(HttpStatus.CREATED).body(scheduleAssembler.toResponse(created));
		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping
	public ResponseEntity<List<ScheduleResponse>> listSchedules(
			@RequestParam(required = false) String deviceId,
			@RequestParam(required = false) String roomId,
			@RequestParam(required = false) Boolean enabled,
			@RequestParam(required = false) String search
	) {
		ListSchedules query = new ListSchedules(search, roomId, enabled, deviceId);
		List<Schedule> schedules = scheduleQueryService.listSchedules(query);
		return ResponseEntity.ok(scheduleAssembler.toResponseList(schedules));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ScheduleResponse> getScheduleById(@PathVariable String id) {
		try {
			Schedule schedule = scheduleQueryService.getScheduleById(new GetScheduleById(id));
			return ResponseEntity.ok(scheduleAssembler.toResponse(schedule));
		} catch (Exception ex) {
			return ResponseEntity.notFound().build();
		}
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ScheduleResponse> updateSchedule(
			@PathVariable String id,
			@Valid @RequestBody UpdateScheduleResource resource
	) {
		try {
			Schedule updated = scheduleCommandService.updateSchedule(scheduleAssembler.toUpdateCommand(id, resource));
			return ResponseEntity.ok(scheduleAssembler.toResponse(updated));
		} catch (Exception ex) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSchedule(@PathVariable String id) {
		try {
			scheduleCommandService.deleteSchedule(new DeleteSchedule(id));
			return ResponseEntity.noContent().build();
		} catch (Exception ex) {
			return ResponseEntity.notFound().build();
		}
	}

	@PatchMapping("/{id}/toggle")
	public ResponseEntity<ScheduleResponse> toggleSchedule(
			@PathVariable String id,
			@Valid @RequestBody ToggleScheduleResource resource
	) {
		try {
			Schedule toggled = scheduleCommandService.toggleSchedule(new ToggleSchedule(id, resource.isEnabled()));
			return ResponseEntity.ok(scheduleAssembler.toResponse(toggled));
		} catch (Exception ex) {
			return ResponseEntity.notFound().build();
		}
	}
}
