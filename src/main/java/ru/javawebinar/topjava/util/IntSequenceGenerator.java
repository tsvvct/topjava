package ru.javawebinar.topjava.util;

import java.util.concurrent.atomic.AtomicInteger;

public class IntSequenceGenerator {

    private final AtomicInteger value;

    public IntSequenceGenerator() {
         this.value = new AtomicInteger(0);
    }

    public Integer getNext() {
        return value.getAndIncrement();
    }
}
