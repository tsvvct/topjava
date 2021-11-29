package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalTimeFormatter implements Formatter<LocalTime> {
    private String pattern = "HH:mm";

    public LocalTimeFormatter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        return StringUtils.hasLength(text) ? LocalTime.parse(text, getFormatter(locale)) : null;
    }

    @Override
    public String print(LocalTime value, Locale locale) {
        return value == null ? "" : value.format(getFormatter(locale));
    }

    protected DateTimeFormatter getFormatter(Locale locale) {
        return DateTimeFormatter.ofPattern(pattern, locale);
    }
}
