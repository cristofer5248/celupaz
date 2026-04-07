package com.chrisgeek.celupaz.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlmaHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static AlmaHistory getAlmaHistorySample1() {
        return new AlmaHistory().id(1L).fecha("fecha1");
    }

    public static AlmaHistory getAlmaHistorySample2() {
        return new AlmaHistory().id(2L).fecha("fecha2");
    }

    public static AlmaHistory getAlmaHistoryRandomSampleGenerator() {
        return new AlmaHistory().id(longCount.incrementAndGet()).fecha(UUID.randomUUID().toString());
    }
}
