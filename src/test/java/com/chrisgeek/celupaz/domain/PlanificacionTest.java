package com.chrisgeek.celupaz.domain;

import static com.chrisgeek.celupaz.domain.AlmaHistoryTestSamples.*;
import static com.chrisgeek.celupaz.domain.PlaniMasterTestSamples.*;
import static com.chrisgeek.celupaz.domain.PlanificacionTestSamples.*;
import static com.chrisgeek.celupaz.domain.PrivilegeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.chrisgeek.celupaz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlanificacionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Planificacion.class);
        Planificacion planificacion1 = getPlanificacionSample1();
        Planificacion planificacion2 = new Planificacion();
        assertThat(planificacion1).isNotEqualTo(planificacion2);

        planificacion2.setId(planificacion1.getId());
        assertThat(planificacion1).isEqualTo(planificacion2);

        planificacion2 = getPlanificacionSample2();
        assertThat(planificacion1).isNotEqualTo(planificacion2);
    }

    @Test
    void almahistoryTest() {
        Planificacion planificacion = getPlanificacionRandomSampleGenerator();
        AlmaHistory almaHistoryBack = getAlmaHistoryRandomSampleGenerator();

        planificacion.setAlmahistory(almaHistoryBack);
        assertThat(planificacion.getAlmahistory()).isEqualTo(almaHistoryBack);

        planificacion.almahistory(null);
        assertThat(planificacion.getAlmahistory()).isNull();
    }

    @Test
    void privilegeTest() {
        Planificacion planificacion = getPlanificacionRandomSampleGenerator();
        Privilege privilegeBack = getPrivilegeRandomSampleGenerator();

        planificacion.setPrivilege(privilegeBack);
        assertThat(planificacion.getPrivilege()).isEqualTo(privilegeBack);

        planificacion.privilege(null);
        assertThat(planificacion.getPrivilege()).isNull();
    }

    @Test
    void planiMasterTest() {
        Planificacion planificacion = getPlanificacionRandomSampleGenerator();
        PlaniMaster planiMasterBack = getPlaniMasterRandomSampleGenerator();

        planificacion.setPlaniMaster(planiMasterBack);
        assertThat(planificacion.getPlaniMaster()).isEqualTo(planiMasterBack);

        planificacion.planiMaster(null);
        assertThat(planificacion.getPlaniMaster()).isNull();
    }
}
