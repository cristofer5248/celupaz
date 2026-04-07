package com.chrisgeek.celupaz.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlmaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Alma getAlmaSample1() {
        return new Alma()
            .id(1L)
            .name("name1")
            .email("email1")
            .phone("phone1")
            .department("department1")
            .municipality("municipality1")
            .colony("colony1")
            .description("description1");
    }

    public static Alma getAlmaSample2() {
        return new Alma()
            .id(2L)
            .name("name2")
            .email("email2")
            .phone("phone2")
            .department("department2")
            .municipality("municipality2")
            .colony("colony2")
            .description("description2");
    }

    public static Alma getAlmaRandomSampleGenerator() {
        return new Alma()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .department(UUID.randomUUID().toString())
            .municipality(UUID.randomUUID().toString())
            .colony(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
