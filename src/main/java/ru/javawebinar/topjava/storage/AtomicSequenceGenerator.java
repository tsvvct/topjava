package ru.javawebinar.topjava.storage;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicSequenceGenerator implements SequenceGenerator {

    private final AtomicInteger value;

    public AtomicSequenceGenerator() {
         this.value = new AtomicInteger(0);
    }

    @Override
    public int getNext() {
        return value.getAndIncrement();
    }
}
