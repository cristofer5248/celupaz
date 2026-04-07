package com.chrisgeek.celupaz.domain;

import static com.chrisgeek.celupaz.domain.PrivilegeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.chrisgeek.celupaz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrivilegeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Privilege.class);
        Privilege privilege1 = getPrivilegeSample1();
        Privilege privilege2 = new Privilege();
        assertThat(privilege1).isNotEqualTo(privilege2);

        privilege2.setId(privilege1.getId());
        assertThat(privilege1).isEqualTo(privilege2);

        privilege2 = getPrivilegeSample2();
        assertThat(privilege1).isNotEqualTo(privilege2);
    }
}
