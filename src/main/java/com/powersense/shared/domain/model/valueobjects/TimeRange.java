package com.powersense.shared.domain.model.valueobjects;

import com.powersense.shared.domain.exceptions.ValidationException;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public final class TimeRange {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm");

    private final LocalTime start;
    private final LocalTime end;

    private TimeRange(LocalTime start, LocalTime end) {
        if (start == null || end == null) throw new ValidationException("Start and end cannot be null");
        this.start = start;
        this.end = end;
        if (start.equals(end)) {
            throw new ValidationException("Start and end cannot be equal");
        }
    }

    public static TimeRange of(LocalTime start, LocalTime end) {
        return new TimeRange(start, end);
    }

    public static TimeRange of(String start, String end) {
        return new TimeRange(LocalTime.parse(start, FMT), LocalTime.parse(end, FMT));
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public boolean contains(LocalTime time) {
        if (time == null) return false;
        if (!crossesMidnight()) {
            return !time.isBefore(start) && time.isBefore(end);
        }
        // crosses midnight: [start..24h) U [00..end)
        return !time.isBefore(start) || time.isBefore(end);
    }

    public boolean overlaps(TimeRange other) {
        Objects.requireNonNull(other, "other");
        // brute-force check: if either contains the other's start or end, or vice versa
        return this.contains(other.start) || this.contains(other.end.minusMinutes(1))
                || other.contains(this.start) || other.contains(this.end.minusMinutes(1));
    }

    public long getDurationInMinutes() {
        if (!crossesMidnight()) {
            return Duration.between(start, end).toMinutes();
        }
        long toMidnight = Duration.between(start, LocalTime.MIDNIGHT).toMinutes();
        long fromMidnight = Duration.between(LocalTime.MIDNIGHT, end).toMinutes();
        return toMidnight + fromMidnight;
    }

    public String toFormattedString() {
        return start.format(FMT) + " - " + end.format(FMT);
    }

    private boolean crossesMidnight() {
        return end.isBefore(start);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeRange that)) return false;
        return start.equals(that.start) && end.equals(that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return toFormattedString();
    }
}
