package com.muller.time;

public enum Precision {
    SECOND,
    MILLISECOND,
    MICROSECOND,
    NANOSECOND;

    public static Precision fromString(String precision) {
        switch (precision.toLowerCase()) {
            case "nanosecond":
                return NANOSECOND;
            case "microsecond":
                return MICROSECOND;
            case "millisecond":
                return MILLISECOND;
            case "second":
            default:
                return SECOND;
        }
    }
}
