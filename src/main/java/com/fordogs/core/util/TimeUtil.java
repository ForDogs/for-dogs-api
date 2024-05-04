package com.fordogs.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.DateTimeException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtil {

    private static final DateTimeFormatter STANDARD_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private static final DateTimeFormatter UTC_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public static String toString(ZonedDateTime zonedDateTime) {
        try {
            if (zonedDateTime.getZone().equals(ZoneOffset.UTC)) {
                return zonedDateTime.format(UTC_FORMATTER);
            } else {
                return zonedDateTime.format(STANDARD_FORMATTER);
            }
        } catch (DateTimeException e) {
            return null;
        }
    }
}
