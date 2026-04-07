package com.chrisgeek.celupaz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * A AlmaHistory.
 */
@Entity
@Table(name = "alma_history")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlmaHistory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha")
    private String fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    private Alma alma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "cellType" }, allowSetters = true)
    private Cell cell;

    @ManyToOne(fetch = FetchType.LAZY)
    private RolCelula rolcelula;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlmaHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFecha() {
        return this.fecha;
    }

    public AlmaHistory fecha(String fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Alma getAlma() {
        return this.alma;
    }

    public void setAlma(Alma alma) {
        this.alma = alma;
    }

    public AlmaHistory alma(Alma alma) {
        this.setAlma(alma);
        return this;
    }

    public Cell getCell() {
        return this.cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public AlmaHistory cell(Cell cell) {
        this.setCell(cell);
        return this;
    }

    public RolCelula getRolcelula() {
        return this.rolcelula;
    }

    public void setRolcelula(RolCelula rolCelula) {
        this.rolcelula = rolCelula;
    }

    public AlmaHistory rolcelula(RolCelula rolCelula) {
        this.setRolcelula(rolCelula);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlmaHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((AlmaHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlmaHistory{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            "}";
    }
}
