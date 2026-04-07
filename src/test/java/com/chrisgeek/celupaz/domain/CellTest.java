package com.chrisgeek.celupaz.domain;

import static com.chrisgeek.celupaz.domain.CellTestSamples.*;
import static com.chrisgeek.celupaz.domain.CellTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.chrisgeek.celupaz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CellTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cell.class);
        Cell cell1 = getCellSample1();
        Cell cell2 = new Cell();
        assertThat(cell1).isNotEqualTo(cell2);

        cell2.setId(cell1.getId());
        assertThat(cell1).isEqualTo(cell2);

        cell2 = getCellSample2();
        assertThat(cell1).isNotEqualTo(cell2);
    }

    @Test
    void cellTypeTest() {
        Cell cell = getCellRandomSampleGenerator();
        CellType cellTypeBack = getCellTypeRandomSampleGenerator();

        cell.setCellType(cellTypeBack);
        assertThat(cell.getCellType()).isEqualTo(cellTypeBack);

        cell.cellType(null);
        assertThat(cell.getCellType()).isNull();
    }
}
