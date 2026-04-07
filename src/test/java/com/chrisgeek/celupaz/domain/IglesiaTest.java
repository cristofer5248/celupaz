package com.chrisgeek.celupaz.domain;

import static com.chrisgeek.celupaz.domain.IglesiaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.chrisgeek.celupaz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IglesiaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Iglesia.class);
        Iglesia iglesia1 = getIglesiaSample1();
        Iglesia iglesia2 = new Iglesia();
        assertThat(iglesia1).isNotEqualTo(iglesia2);

        iglesia2.setId(iglesia1.getId());
        assertThat(iglesia1).isEqualTo(iglesia2);

        iglesia2 = getIglesiaSample2();
        assertThat(iglesia1).isNotEqualTo(iglesia2);
    }
}
