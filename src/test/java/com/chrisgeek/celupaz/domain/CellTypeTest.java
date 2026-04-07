package com.chrisgeek.celupaz.domain;

import static com.chrisgeek.celupaz.domain.CellTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.chrisgeek.celupaz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CellTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CellType.class);
        CellType cellType1 = getCellTypeSample1();
        CellType cellType2 = new CellType();
        assertThat(cellType1).isNotEqualTo(cellType2);

        cellType2.setId(cellType1.getId());
        assertThat(cellType1).isEqualTo(cellType2);

        cellType2 = getCellTypeSample2();
        assertThat(cellType1).isNotEqualTo(cellType2);
    }
}
