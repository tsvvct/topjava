package ru.javawebinar.topjava.storage;

import java.util.List;

public interface StorageStrategy<T> {
    T getById(int id);

    List<T> getAll();

    void delete(int id);

    void update(T item);

    void add(T item);

    int getNextId();
}
