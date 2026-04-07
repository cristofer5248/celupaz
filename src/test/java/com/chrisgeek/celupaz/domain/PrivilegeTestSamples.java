package com.chrisgeek.celupaz.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PrivilegeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Privilege getPrivilegeSample1() {
        return new Privilege().id(1L).name("name1");
    }

    public static Privilege getPrivilegeSample2() {
        return new Privilege().id(2L).name("name2");
    }

    public static Privilege getPrivilegeRandomSampleGenerator() {
        return new Privilege().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
