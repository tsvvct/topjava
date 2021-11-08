package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Function;

@Component
@Profile("postgres")
public class PostgresHsqldbDateTimeConverter implements Function<LocalDateTime, LocalDateTime> {
    @Override
    public LocalDateTime apply(LocalDateTime localDateTime) {
        return localDateTime;
    }

    public LocalDateTime convert(LocalDateTime valueToConvert) {
        return valueToConvert;
    }
}
