package com.chrisgeek.celupaz.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PlanificacionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Planificacion getPlanificacionSample1() {
        return new Planificacion().id(1L);
    }

    public static Planificacion getPlanificacionSample2() {
        return new Planificacion().id(2L);
    }

    public static Planificacion getPlanificacionRandomSampleGenerator() {
        return new Planificacion().id(longCount.incrementAndGet());
    }
}
