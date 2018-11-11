package com.muller.time;

import java.time.*;
import java.util.concurrent.TimeUnit;

public class LocalDateTimeUtils {
    /**
     * Convert a LocalDateTime into a timestamp with a specific precision
     * @param time a LocalDateTime object
     * @param precision precision in which the result timestamp should be represented
     * @return timestamp
     */
    public static long toTimestamp(LocalDateTime time, Precision precision) {
        return toTimestamp(time, precision, ZoneOffset.systemDefault());
    }

    /**
     * Convert a LocalDateTime into a timestamp with a specific precision
     * @param time a LocalDateTime object in @fromZone
     * @param precision precision in which the result timestamp should be represented
     * @param fromZone zone in which the LocalDateTime is represented
     * @return timestamp
     */
    public static long toTimestamp(LocalDateTime time, Precision precision, ZoneId fromZone) {
        long epochSeconds = time.atZone(fromZone).toEpochSecond();

        switch (precision) {
            case NANOSECOND:
                return TimeUnit.SECONDS.toNanos(epochSeconds) + time.getNano();
            case MICROSECOND:
                return TimeUnit.SECONDS.toMicros(epochSeconds) + TimeUnit.NANOSECONDS.toMicros(time.getNano());
            case MILLISECOND:
                return TimeUnit.SECONDS.toMillis(epochSeconds) + TimeUnit.NANOSECONDS.toMillis(time.getNano());
            case SECOND:
            default:
                return epochSeconds;
        }
    }

    /**
     * Convert a timestamp from a specific precision into a LocalDateTime
     * @param timestamp a timestamp
     * @param precision precision in which the timestamp is represented
     * @return LocalDateTime
     */
    public static LocalDateTime fromTimestamp(long timestamp, Precision precision) {
        return fromTimestamp(timestamp, precision, ZoneOffset.systemDefault());
    }

    /**
     * Convert a timestamp from a specific precision into a LocalDateTime
     * @param timestamp a timestamp
     * @param precision precision in which the timestamp is represented
     * @param toZone zone in which the result LocalDateTime should be represented
     * @return LocalDateTime in @toZone
     */
    public static LocalDateTime fromTimestamp(long timestamp, Precision precision, ZoneId toZone) {
        LocalDateTime time = ZonedDateTime.ofInstant(Instant.EPOCH, toZone).toLocalDateTime();

        switch (precision) {
            case NANOSECOND:
                return time.plusNanos(timestamp);
            case MICROSECOND:
                return time.plusNanos(TimeUnit.MICROSECONDS.toNanos(timestamp));
            case MILLISECOND:
                return time.plusNanos(TimeUnit.MILLISECONDS.toNanos(timestamp));
            case SECOND:
            default:
                return time.plusSeconds(timestamp);
        }
    }
}
