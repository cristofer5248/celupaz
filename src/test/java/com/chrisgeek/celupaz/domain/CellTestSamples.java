package com.chrisgeek.celupaz.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CellTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Cell getCellSample1() {
        return new Cell().id(1L).name("name1").description("description1").sector(1).lider("lider1").cordinador("cordinador1");
    }

    public static Cell getCellSample2() {
        return new Cell().id(2L).name("name2").description("description2").sector(2).lider("lider2").cordinador("cordinador2");
    }

    public static Cell getCellRandomSampleGenerator() {
        return new Cell()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .sector(intCount.incrementAndGet())
            .lider(UUID.randomUUID().toString())
            .cordinador(UUID.randomUUID().toString());
    }
}
