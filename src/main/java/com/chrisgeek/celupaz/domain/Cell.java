package com.chrisgeek.celupaz.domain;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Cell.
 */
@Entity
@Table(name = "cell")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cell implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "description")
    private String description;

    @Column(name = "sector")
    private Integer sector;

    @Column(name = "lider")
    private String lider;

    @Column(name = "cordinador")
    private String cordinador;

    @ManyToOne(fetch = FetchType.LAZY)
    private CellType cellType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cell id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Cell name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Cell startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getDescription() {
        return this.description;
    }

    public Cell description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSector() {
        return this.sector;
    }

    public Cell sector(Integer sector) {
        this.setSector(sector);
        return this;
    }

    public void setSector(Integer sector) {
        this.sector = sector;
    }

    public String getLider() {
        return this.lider;
    }

    public Cell lider(String lider) {
        this.setLider(lider);
        return this;
    }

    public void setLider(String lider) {
        this.lider = lider;
    }

    public String getCordinador() {
        return this.cordinador;
    }

    public Cell cordinador(String cordinador) {
        this.setCordinador(cordinador);
        return this;
    }

    public void setCordinador(String cordinador) {
        this.cordinador = cordinador;
    }

    public CellType getCellType() {
        return this.cellType;
    }

    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }

    public Cell cellType(CellType cellType) {
        this.setCellType(cellType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cell)) {
            return false;
        }
        return getId() != null && getId().equals(((Cell) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cell{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", sector=" + getSector() +
            ", lider='" + getLider() + "'" +
            ", cordinador='" + getCordinador() + "'" +
            "}";
    }
}
