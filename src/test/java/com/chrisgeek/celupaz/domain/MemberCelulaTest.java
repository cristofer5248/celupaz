package com.chrisgeek.celupaz.domain;

import static com.chrisgeek.celupaz.domain.CellTestSamples.*;
import static com.chrisgeek.celupaz.domain.MemberCelulaTestSamples.*;
import static com.chrisgeek.celupaz.domain.MemberTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.chrisgeek.celupaz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MemberCelulaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemberCelula.class);
        MemberCelula memberCelula1 = getMemberCelulaSample1();
        MemberCelula memberCelula2 = new MemberCelula();
        assertThat(memberCelula1).isNotEqualTo(memberCelula2);

        memberCelula2.setId(memberCelula1.getId());
        assertThat(memberCelula1).isEqualTo(memberCelula2);

        memberCelula2 = getMemberCelulaSample2();
        assertThat(memberCelula1).isNotEqualTo(memberCelula2);
    }

    @Test
    void memberTest() {
        MemberCelula memberCelula = getMemberCelulaRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        memberCelula.setMember(memberBack);
        assertThat(memberCelula.getMember()).isEqualTo(memberBack);

        memberCelula.member(null);
        assertThat(memberCelula.getMember()).isNull();
    }

    @Test
    void cellTest() {
        MemberCelula memberCelula = getMemberCelulaRandomSampleGenerator();
        Cell cellBack = getCellRandomSampleGenerator();

        memberCelula.setCell(cellBack);
        assertThat(memberCelula.getCell()).isEqualTo(cellBack);

        memberCelula.cell(null);
        assertThat(memberCelula.getCell()).isNull();
    }
}
