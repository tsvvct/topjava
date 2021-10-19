package ru.javawebinar.topjava.util;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Function;
import java.util.function.Predicate;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T> T parseOrNull(Function<String, T> parser, String value) {
        return (value.isEmpty()) ? null : parser.apply(value);
    }

    public static <T, R extends Comparable<R>> Predicate<T> getValueFilter(@NonNull Function<T, R> getValueFunction,
                                                                           @Nullable R valueFrom,
                                                                           @Nullable R valueTo,
                                                                           boolean includeLeftBound,
                                                                           boolean includeRightBound) {
        Predicate<T> filter = elem -> true;

        if (valueFrom != null) {
            filter = filter.and(elem -> getValueFunction.apply(elem).compareTo(valueFrom) > 0);
            if (includeLeftBound) {
                filter = filter.or(elem -> getValueFunction.apply(elem).compareTo(valueFrom) == 0);
            }
        }

        if (valueTo != null) {
            filter = filter.and(elem -> getValueFunction.apply(elem).compareTo(valueTo) < 0);
            if (includeRightBound) {
                filter = filter.or(elem -> getValueFunction.apply(elem).compareTo(valueTo) == 0);
            }
        }

        // для включения в результаты поиска граничного значения для вырожденного полуоткрытого интервала [N;N)
        // можно раскомментировать следующие строки
//        if (valueFrom != null && valueTo != null) {
//            filter = filter.or(elem -> valueFrom.compareTo(valueTo) == 0 && getValueFunction.apply(elem).compareTo(valueTo) == 0);
//        }

        return filter;
    }

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    public static LocalDate parseDate(String value) {
        if (value == null) {
            return null;
        }
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    public static LocalTime parseTime(String value) {
        if (value == null) {
            return null;
        }
        try {
            return LocalTime.parse(value);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}