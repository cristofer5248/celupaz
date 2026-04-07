package com.chrisgeek.celupaz.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IglesiaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Iglesia getIglesiaSample1() {
        return new Iglesia().id(1L).name("name1");
    }

    public static Iglesia getIglesiaSample2() {
        return new Iglesia().id(2L).name("name2");
    }

    public static Iglesia getIglesiaRandomSampleGenerator() {
        return new Iglesia().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
