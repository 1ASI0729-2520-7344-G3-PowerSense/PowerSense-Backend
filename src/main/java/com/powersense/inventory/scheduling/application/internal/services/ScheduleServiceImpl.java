package com.powersense.inventory.scheduling.application.internal.services;

import com.powersense.inventory.scheduling.domain.exceptions.DuplicateScheduleException;
import com.powersense.inventory.scheduling.domain.exceptions.InvalidTimeSlotException;
import com.powersense.inventory.scheduling.domain.model.aggregates.Schedule;
import com.powersense.inventory.scheduling.domain.model.entities.ScheduleEntry;
import com.powersense.inventory.scheduling.domain.model.valueobjects.DayOfWeek;
import com.powersense.inventory.scheduling.domain.services.ScheduleService;
import com.powersense.inventory.scheduling.infrastructure.persistence.jpa.repositories.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public void validateNoDeviceDuplication(String deviceId, String excludeScheduleId) {
        // Verificar si ya existe un schedule para este deviceId (excepto el excluido)
        var existing = scheduleRepository.findByDeviceId(deviceId);

        if (existing.isPresent()) {
            // Si existe y NO es el que estamos excluyendo, es duplicado
            if (excludeScheduleId == null || !existing.get().getId().equals(excludeScheduleId)) {
                throw new DuplicateScheduleException(deviceId);
            }
        }
    }

    @Override
    public void validateTimeSlotCoherence(List<ScheduleEntry> entries) {
        if (entries == null || entries.isEmpty()) {
            return; // Sin entries, no hay conflictos
        }

        // Validar que no haya dos entries con el MISMO action, MISMA hora y días solapados
        for (int i = 0; i < entries.size(); i++) {
            for (int j = i + 1; j < entries.size(); j++) {
                ScheduleEntry entry1 = entries.get(i);
                ScheduleEntry entry2 = entries.get(j);

                // Verificar si tienen el mismo action
                boolean sameAction = entry1.getAction().equals(entry2.getAction());

                // Verificar si tienen la misma hora
                LocalTime time1 = entry1.getTime().toLocalTime();
                LocalTime time2 = entry2.getTime().toLocalTime();
                boolean sameTime = time1.equals(time2);

                if (sameAction && sameTime) {
                    // Verificar si tienen días en común
                    boolean hasSameDays = hasOverlappingDays(entry1.getDays(), entry2.getDays());

                    if (hasSameDays) {
                        throw new InvalidTimeSlotException(
                                String.format("Conflicting schedule entries: %s at %s on overlapping days",
                                        entry1.getAction(), time1)
                        );
                    }
                }
            }
        }
    }

    @Override
    public boolean hasScheduleConflicts(Schedule schedule) {
        // Verificar si el schedule tiene conflictos internos
        try {
            schedule.validateNoTimeConflicts();
            return false; // No hay conflictos
        } catch (IllegalStateException e) {
            return true; // Hay conflictos
        }
    }

    // Método auxiliar para detectar días solapados
    private boolean hasOverlappingDays(List<DayOfWeek> days1, List<DayOfWeek> days2) {
        return days1.stream().anyMatch(days2::contains);
    }
}
