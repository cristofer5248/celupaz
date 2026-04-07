package com.chrisgeek.celupaz.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MemberTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Member getMemberSample1() {
        return new Member()
            .id(1L)
            .name("name1")
            .email("email1")
            .phone("phone1")
            .department("department1")
            .municipality("municipality1")
            .colony("colony1")
            .relacion("relacion1");
    }

    public static Member getMemberSample2() {
        return new Member()
            .id(2L)
            .name("name2")
            .email("email2")
            .phone("phone2")
            .department("department2")
            .municipality("municipality2")
            .colony("colony2")
            .relacion("relacion2");
    }

    public static Member getMemberRandomSampleGenerator() {
        return new Member()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .department(UUID.randomUUID().toString())
            .municipality(UUID.randomUUID().toString())
            .colony(UUID.randomUUID().toString())
            .relacion(UUID.randomUUID().toString());
    }
}
