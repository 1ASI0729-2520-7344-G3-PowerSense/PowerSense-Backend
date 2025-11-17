package com.powersense.shared.infrastructure.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public final class DateUtils {

	private DateUtils() {
	}

	public static String toISO8601(Instant instant) {
		return instant.toString();
	}

	public static String toISO8601(LocalDateTime dateTime) {
		return dateTime.atZone(ZoneId.systemDefault()).toInstant().toString();
	}

	public static Instant parseISO8601(String iso8601) {
		return Instant.parse(iso8601);
	}

	public static LocalDateTime toLocalDateTime(Instant instant, ZoneId zoneId) {
		return LocalDateTime.ofInstant(instant, zoneId);
	}

	public static boolean isBetween(LocalTime time, LocalTime start, LocalTime end) {
		return !time.isBefore(start) && !time.isAfter(end);
	}

	public static long daysBetween(LocalDate start, LocalDate end) {
		return ChronoUnit.DAYS.between(start, end);
	}
}
