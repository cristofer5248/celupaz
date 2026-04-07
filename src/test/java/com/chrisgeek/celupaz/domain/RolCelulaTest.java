package com.chrisgeek.celupaz.domain;

import static com.chrisgeek.celupaz.domain.RolCelulaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.chrisgeek.celupaz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RolCelulaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RolCelula.class);
        RolCelula rolCelula1 = getRolCelulaSample1();
        RolCelula rolCelula2 = new RolCelula();
        assertThat(rolCelula1).isNotEqualTo(rolCelula2);

        rolCelula2.setId(rolCelula1.getId());
        assertThat(rolCelula1).isEqualTo(rolCelula2);

        rolCelula2 = getRolCelulaSample2();
        assertThat(rolCelula1).isNotEqualTo(rolCelula2);
    }
}
