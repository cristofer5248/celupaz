package com.chrisgeek.celupaz.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class MemberCelulaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static MemberCelula getMemberCelulaSample1() {
        return new MemberCelula().id(1L);
    }

    public static MemberCelula getMemberCelulaSample2() {
        return new MemberCelula().id(2L);
    }

    public static MemberCelula getMemberCelulaRandomSampleGenerator() {
        return new MemberCelula().id(longCount.incrementAndGet());
    }
}
