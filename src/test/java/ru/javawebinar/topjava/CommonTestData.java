package ru.javawebinar.topjava;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class CommonTestData {
    public static final int NOT_FOUND_ID = 100;
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int USER_MEAL_ID = START_SEQ + 2;

    public static <T> void assertMatch(T actual, T expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static <T> void assertMatch(T actual, T expected, String... ignoreFields) {
        assertThat(actual).usingRecursiveComparison().ignoringFields(ignoreFields).isEqualTo(expected);
    }

    public static <T> void assertMatch(Iterable<T> actual, Iterable<T> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

    public static <T> void assertMatch(Iterable<T> actual, Iterable<T> expected, String[] ignoreFields) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields(ignoreFields).isEqualTo(expected);
    }
}
