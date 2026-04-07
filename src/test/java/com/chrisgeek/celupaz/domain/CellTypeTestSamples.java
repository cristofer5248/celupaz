package com.chrisgeek.celupaz.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CellTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static CellType getCellTypeSample1() {
        return new CellType().id(1L).name("name1");
    }

    public static CellType getCellTypeSample2() {
        return new CellType().id(2L).name("name2");
    }

    public static CellType getCellTypeRandomSampleGenerator() {
        return new CellType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
