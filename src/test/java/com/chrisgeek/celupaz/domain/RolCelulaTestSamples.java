package com.chrisgeek.celupaz.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RolCelulaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static RolCelula getRolCelulaSample1() {
        return new RolCelula().id(1L).name("name1");
    }

    public static RolCelula getRolCelulaSample2() {
        return new RolCelula().id(2L).name("name2");
    }

    public static RolCelula getRolCelulaRandomSampleGenerator() {
        return new RolCelula().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
