package com.powersense.shared.domain.model.valueobjects;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class Timestamp {
    private final Instant instant;

    private Timestamp(Instant instant) {
        if (instant == null) throw new IllegalArgumentException("Instant cannot be null");
        this.instant = instant;
    }

    public static Timestamp now() {
        return new Timestamp(Instant.now());
    }

    public static Timestamp of(Instant instant) {
        return new Timestamp(instant);
    }

    public static Timestamp parse(String iso8601) {
        return new Timestamp(Instant.parse(iso8601));
    }

    public Instant getInstant() {
        return instant;
    }

    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    public LocalDate toLocalDate() {
        return toLocalDateTime().toLocalDate();
    }

    public boolean isBefore(Timestamp other) {
        return this.instant.isBefore(other.instant);
    }

    public boolean isAfter(Timestamp other) {
        return this.instant.isAfter(other.instant);
    }

    public String toISO8601String() {
        return instant.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Timestamp that)) return false;
        return instant.equals(that.instant);
    }

    @Override
    public int hashCode() {
        return instant.hashCode();
    }

    @Override
    public String toString() {
        return toISO8601String();
    }
}
