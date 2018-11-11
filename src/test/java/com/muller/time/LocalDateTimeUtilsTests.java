package com.muller.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class LocalDateTimeUtilsTests {
    @Test
    @DisplayName("Timestamp remains equal after converting from LocalDateTime")
    void convertLocalDateTimeToTimestamp() {
        long originalTimestamp = 946688461L;
        LocalDateTime localDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(originalTimestamp), ZoneId.systemDefault()).toLocalDateTime();
        long timestamp = LocalDateTimeUtils.toTimestamp(localDateTime, Precision.SECOND);

        assertEquals(originalTimestamp, timestamp);
    }

    @Test
    @DisplayName("Timestamp remains equal after converting to and from LocalDateTime using different timezones")
    void convertTimestampToLocalDateTimeAndBackInTimezones() {
        long originalTimestamp = 946688461L;
        LocalDateTime timeUtc = LocalDateTimeUtils.fromTimestamp(originalTimestamp, Precision.SECOND, ZoneOffset.UTC);
        LocalDateTime timeLocal = LocalDateTimeUtils.fromTimestamp(originalTimestamp, Precision.SECOND, ZoneId.of("Europe/Helsinki"));

        long timestampUtc = LocalDateTimeUtils.toTimestamp(timeUtc, Precision.SECOND, ZoneOffset.UTC);
        long timestampLocal = LocalDateTimeUtils.toTimestamp(timeLocal, Precision.SECOND, ZoneId.of("Europe/Helsinki"));

        assertAll("timezone conversion",
                () -> assertNotEquals(timeUtc, timeLocal, "Dates match so test is invalid"),
                () -> assertEquals(timestampUtc, timestampLocal)
        );
    }

    @Test
    @DisplayName("LocalDateTime remains equal after converting from timestamp")
    void convertTimestampToLocalDateTime() {
        LocalDateTime time = LocalDateTime.of(2000, 1, 1, 1, 1, 1);
        long timestamp = ZonedDateTime.of(time, ZoneId.systemDefault()).toEpochSecond();

        LocalDateTime convertedTime = LocalDateTimeUtils.fromTimestamp(timestamp, Precision.SECOND);
        assertEquals(time, convertedTime);
    }

    @Test
    @DisplayName("Timestamp precision is stored correctly in LocalDateTime after conversion")
    void convertTimestampValidatePrecision() {
        long nanoTimestamp = 946688461118123456L;
        long microTimestamp = 946688461118123L;
        long milliTimestamp = 946688461118L;
        long timestamp = 946688461L;

        LocalDateTime nanoDateTime = LocalDateTimeUtils.fromTimestamp(nanoTimestamp, Precision.NANOSECOND);
        LocalDateTime microDateTime = LocalDateTimeUtils.fromTimestamp(microTimestamp, Precision.MICROSECOND);
        LocalDateTime milliDateTime = LocalDateTimeUtils.fromTimestamp(milliTimestamp, Precision.MILLISECOND);
        LocalDateTime dateTime = LocalDateTimeUtils.fromTimestamp(timestamp, Precision.SECOND);

        long nanoResult = TimeUnit.SECONDS.toNanos(nanoDateTime.atZone(ZoneId.systemDefault()).toEpochSecond()) + nanoDateTime.getNano();
        long microResult = TimeUnit.SECONDS.toMicros(microDateTime.atZone(ZoneId.systemDefault()).toEpochSecond()) + TimeUnit.NANOSECONDS.toMicros(nanoDateTime.getNano());
        long milliResult = TimeUnit.SECONDS.toMillis(milliDateTime.atZone(ZoneId.systemDefault()).toEpochSecond()) + TimeUnit.NANOSECONDS.toMillis(nanoDateTime.getNano());
        long secondResult = dateTime.atZone(ZoneId.systemDefault()).toEpochSecond();

        assertAll("precision",
                () -> assertEquals(nanoTimestamp, nanoResult, "Nanosecond precision incorrect"),
                () -> assertEquals(microTimestamp, microResult, "microsecond precision incorrect"),
                () -> assertEquals(milliTimestamp, milliResult, "millisecond precision incorrect"),
                () -> assertEquals(timestamp, secondResult, "second precision incorrect")
        );
    }
}
