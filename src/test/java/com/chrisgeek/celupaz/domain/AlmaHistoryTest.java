package com.chrisgeek.celupaz.domain;

import static com.chrisgeek.celupaz.domain.AlmaHistoryTestSamples.*;
import static com.chrisgeek.celupaz.domain.AlmaTestSamples.*;
import static com.chrisgeek.celupaz.domain.CellTestSamples.*;
import static com.chrisgeek.celupaz.domain.RolCelulaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.chrisgeek.celupaz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlmaHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlmaHistory.class);
        AlmaHistory almaHistory1 = getAlmaHistorySample1();
        AlmaHistory almaHistory2 = new AlmaHistory();
        assertThat(almaHistory1).isNotEqualTo(almaHistory2);

        almaHistory2.setId(almaHistory1.getId());
        assertThat(almaHistory1).isEqualTo(almaHistory2);

        almaHistory2 = getAlmaHistorySample2();
        assertThat(almaHistory1).isNotEqualTo(almaHistory2);
    }

    @Test
    void almaTest() {
        AlmaHistory almaHistory = getAlmaHistoryRandomSampleGenerator();
        Alma almaBack = getAlmaRandomSampleGenerator();

        almaHistory.setAlma(almaBack);
        assertThat(almaHistory.getAlma()).isEqualTo(almaBack);

        almaHistory.alma(null);
        assertThat(almaHistory.getAlma()).isNull();
    }

    @Test
    void cellTest() {
        AlmaHistory almaHistory = getAlmaHistoryRandomSampleGenerator();
        Cell cellBack = getCellRandomSampleGenerator();

        almaHistory.setCell(cellBack);
        assertThat(almaHistory.getCell()).isEqualTo(cellBack);

        almaHistory.cell(null);
        assertThat(almaHistory.getCell()).isNull();
    }

    @Test
    void rolcelulaTest() {
        AlmaHistory almaHistory = getAlmaHistoryRandomSampleGenerator();
        RolCelula rolCelulaBack = getRolCelulaRandomSampleGenerator();

        almaHistory.setRolcelula(rolCelulaBack);
        assertThat(almaHistory.getRolcelula()).isEqualTo(rolCelulaBack);

        almaHistory.rolcelula(null);
        assertThat(almaHistory.getRolcelula()).isNull();
    }
}
