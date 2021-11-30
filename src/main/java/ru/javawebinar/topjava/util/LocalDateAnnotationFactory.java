package ru.javawebinar.topjava.util;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LocalDateAnnotationFactory implements
        AnnotationFormatterFactory<LocalDateFormat> {

    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(Arrays.asList(LocalDate.class));
    }

    @Override
    public Printer<?> getPrinter(LocalDateFormat annotation, Class<?> fieldType) {
        return getLocalDateFormatter(annotation, fieldType);
    }

    @Override
    public Parser<?> getParser(LocalDateFormat annotation, Class<?> fieldType) {
        return getLocalDateFormatter(annotation, fieldType);
    }

    private LocalDateFormatter getLocalDateFormatter(LocalDateFormat annotation,
                                                     Class<?> fieldType) {
        LocalDateFormatter formatter = new LocalDateFormatter(annotation.pattern());
        return formatter;
    }
}
