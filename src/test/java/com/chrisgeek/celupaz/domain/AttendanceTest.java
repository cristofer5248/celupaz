package com.chrisgeek.celupaz.domain;

import static com.chrisgeek.celupaz.domain.AttendanceTestSamples.*;
import static com.chrisgeek.celupaz.domain.MemberCelulaTestSamples.*;
import static com.chrisgeek.celupaz.domain.PlanificacionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.chrisgeek.celupaz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttendanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attendance.class);
        Attendance attendance1 = getAttendanceSample1();
        Attendance attendance2 = new Attendance();
        assertThat(attendance1).isNotEqualTo(attendance2);

        attendance2.setId(attendance1.getId());
        assertThat(attendance1).isEqualTo(attendance2);

        attendance2 = getAttendanceSample2();
        assertThat(attendance1).isNotEqualTo(attendance2);
    }

    @Test
    void membercelulaTest() {
        Attendance attendance = getAttendanceRandomSampleGenerator();
        MemberCelula memberCelulaBack = getMemberCelulaRandomSampleGenerator();

        attendance.setMembercelula(memberCelulaBack);
        assertThat(attendance.getMembercelula()).isEqualTo(memberCelulaBack);

        attendance.membercelula(null);
        assertThat(attendance.getMembercelula()).isNull();
    }

    @Test
    void planificacionTest() {
        Attendance attendance = getAttendanceRandomSampleGenerator();
        Planificacion planificacionBack = getPlanificacionRandomSampleGenerator();

        attendance.setPlanificacion(planificacionBack);
        assertThat(attendance.getPlanificacion()).isEqualTo(planificacionBack);

        attendance.planificacion(null);
        assertThat(attendance.getPlanificacion()).isNull();
    }
}
