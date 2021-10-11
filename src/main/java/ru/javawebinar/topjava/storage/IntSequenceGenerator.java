package ru.javawebinar.topjava.storage;

import java.util.concurrent.atomic.AtomicInteger;

public class IntSequenceGenerator implements SequenceGenerator<Integer> {

    private final AtomicInteger value;

    public IntSequenceGenerator() {
         this.value = new AtomicInteger(0);
    }

    @Override
    public Integer getNext() {
        return value.getAndIncrement();
    }
}
