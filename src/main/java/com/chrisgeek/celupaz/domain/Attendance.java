package com.chrisgeek.celupaz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Attendance.
 */
@Entity
@Table(name = "attendance")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Attendance implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "member", "cell" }, allowSetters = true)
    private MemberCelula membercelula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "almahistory", "privilege", "planiMaster" }, allowSetters = true)
    private Planificacion planificacion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Attendance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Attendance fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public MemberCelula getMembercelula() {
        return this.membercelula;
    }

    public void setMembercelula(MemberCelula memberCelula) {
        this.membercelula = memberCelula;
    }

    public Attendance membercelula(MemberCelula memberCelula) {
        this.setMembercelula(memberCelula);
        return this;
    }

    public Planificacion getPlanificacion() {
        return this.planificacion;
    }

    public void setPlanificacion(Planificacion planificacion) {
        this.planificacion = planificacion;
    }

    public Attendance planificacion(Planificacion planificacion) {
        this.setPlanificacion(planificacion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attendance)) {
            return false;
        }
        return getId() != null && getId().equals(((Attendance) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Attendance{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            "}";
    }
}
