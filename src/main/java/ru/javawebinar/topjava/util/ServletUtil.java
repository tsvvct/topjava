package ru.javawebinar.topjava.util;

import java.time.format.DateTimeFormatter;

public class ServletUtil {
    public static final String DATE_TIME_PICKER_FORMAT = "Y-m-d H:i";
    public static final String DATE_TIME_FORMAT_FOR_VIEW = "yyyy-MM-dd HH:mm";
    public static final String DATE_TIME_FORMAT_FROM_STORAGE = "yyyy-MM-dd'T'HH:mm";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_FOR_VIEW);
}
