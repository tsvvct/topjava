package ru.javawebinar.topjava.storage;

public interface SequenceGenerator<T extends Number> {
    T getNext();
}
