package ru.javawebinar.topjava;

import org.springframework.lang.NonNull;
import org.springframework.test.context.ActiveProfilesResolver;

//http://stackoverflow.com/questions/23871255/spring-profiles-simple-example-of-activeprofilesresolver
public class ActiveDbProfileResolver implements ActiveProfilesResolver {
    @Override
    public @NonNull
    String[] resolve(@NonNull Class<?> aClass) {
        switch (aClass.getSimpleName()) {
            case "MealJdbcServiceTest", "UserJdbcServiceTest" -> {
                return new String[]{Profiles.getActiveDbProfile(), Profiles.JDBC};
            }
            case "MealJpaServiceTest", "UserJpaServiceTest" -> {
                return new String[]{Profiles.getActiveDbProfile(), Profiles.JPA};
            }
            case "MealDataJpaServiceTest", "UserDataJpaServiceTest" -> {
                return new String[]{Profiles.getActiveDbProfile(), Profiles.DATAJPA};
            }
            default -> {
                return new String[]{Profiles.getActiveDbProfile(), Profiles.REPOSITORY_IMPLEMENTATION};
            }
        }
    }
}
