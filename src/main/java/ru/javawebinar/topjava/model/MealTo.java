package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;

public class MealTo {
    private int id;

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isExcess() {
        return excess;
    }

    private final boolean excess;

    public MealTo(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.id = 0;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public MealTo(int id, LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}
