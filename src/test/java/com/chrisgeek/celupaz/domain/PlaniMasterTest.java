package com.chrisgeek.celupaz.domain;

import static com.chrisgeek.celupaz.domain.PlaniMasterTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.chrisgeek.celupaz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlaniMasterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlaniMaster.class);
        PlaniMaster planiMaster1 = getPlaniMasterSample1();
        PlaniMaster planiMaster2 = new PlaniMaster();
        assertThat(planiMaster1).isNotEqualTo(planiMaster2);

        planiMaster2.setId(planiMaster1.getId());
        assertThat(planiMaster1).isEqualTo(planiMaster2);

        planiMaster2 = getPlaniMasterSample2();
        assertThat(planiMaster1).isNotEqualTo(planiMaster2);
    }
}
