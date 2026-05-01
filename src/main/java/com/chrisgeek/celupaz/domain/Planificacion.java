package com.chrisgeek.celupaz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Planificacion.
 */
@Entity
@Table(name = "planificacion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Planificacion implements Serializable {

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
    //    @JsonIgnoreProperties(value = { "alma", "cell", "rolcelula" }, allowSetters = true)
    @JsonIgnoreProperties(value = { "cell", "rolcelula" }, allowSetters = true)
    private AlmaHistory almahistory;

    @ManyToOne(fetch = FetchType.LAZY)
    private Privilege privilege;

    @ManyToOne(fetch = FetchType.LAZY)
    private PlaniMaster planiMaster;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Planificacion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Planificacion fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public AlmaHistory getAlmahistory() {
        return this.almahistory;
    }

    public void setAlmahistory(AlmaHistory almaHistory) {
        this.almahistory = almaHistory;
    }

    public Planificacion almahistory(AlmaHistory almaHistory) {
        this.setAlmahistory(almaHistory);
        return this;
    }

    public Privilege getPrivilege() {
        return this.privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    public Planificacion privilege(Privilege privilege) {
        this.setPrivilege(privilege);
        return this;
    }

    public PlaniMaster getPlaniMaster() {
        return this.planiMaster;
    }

    public void setPlaniMaster(PlaniMaster planiMaster) {
        this.planiMaster = planiMaster;
    }

    public Planificacion planiMaster(PlaniMaster planiMaster) {
        this.setPlaniMaster(planiMaster);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Planificacion)) {
            return false;
        }
        return getId() != null && getId().equals(((Planificacion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Planificacion{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            "}";
    }
}
