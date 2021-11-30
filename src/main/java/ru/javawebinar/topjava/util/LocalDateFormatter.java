package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {

    private String pattern = "yyyy-MM-dd";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

    public LocalDateFormatter() {
    }

    public LocalDateFormatter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return StringUtils.hasLength(text) ? LocalDate.parse(text, getFormatter(locale)) : null;
    }

    @Override
    public String print(LocalDate value, Locale locale) {
        return value == null ? "" : value.format(getFormatter(locale));
    }

    protected DateTimeFormatter getFormatter(Locale locale) {
        return formatter.withLocale(locale);
    }
}
