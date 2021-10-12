package ru.javawebinar.topjava.web;

public enum Action {
    CREATE, LIST, UPDATE, DELETE;

    public static Action getOrDefault(String value) {
        if (value == null) {
            return LIST;
        } else {
            return valueOf(value.toUpperCase());
        }
    }
}
