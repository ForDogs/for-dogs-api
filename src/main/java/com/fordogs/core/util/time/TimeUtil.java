package com.fordogs.core.util.time;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtil {

    private static final String STANDARD_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(STANDARD_FORMAT);
        return localDateTime.format(formatter);
    }

    public static LocalDateTime toLocalDateTime(Long epochMilli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
    }
}
