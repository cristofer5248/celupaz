package com.chrisgeek.celupaz.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PlaniMasterTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static PlaniMaster getPlaniMasterSample1() {
        return new PlaniMaster().id(1L).note("note1").doneby("doneby1");
    }

    public static PlaniMaster getPlaniMasterSample2() {
        return new PlaniMaster().id(2L).note("note2").doneby("doneby2");
    }

    public static PlaniMaster getPlaniMasterRandomSampleGenerator() {
        return new PlaniMaster().id(longCount.incrementAndGet()).note(UUID.randomUUID().toString()).doneby(UUID.randomUUID().toString());
    }
}
