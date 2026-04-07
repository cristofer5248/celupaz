package com.chrisgeek.celupaz.domain;

import static com.chrisgeek.celupaz.domain.AlmaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.chrisgeek.celupaz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alma.class);
        Alma alma1 = getAlmaSample1();
        Alma alma2 = new Alma();
        assertThat(alma1).isNotEqualTo(alma2);

        alma2.setId(alma1.getId());
        assertThat(alma1).isEqualTo(alma2);

        alma2 = getAlmaSample2();
        assertThat(alma1).isNotEqualTo(alma2);
    }
}
