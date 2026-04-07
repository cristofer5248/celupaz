package com.chrisgeek.celupaz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A MemberCelula.
 */
@Entity
@Table(name = "member_celula")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MemberCelula implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha_creada", nullable = false)
    private LocalDate fechaCreada;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "iglesia" }, allowSetters = true)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "cellType" }, allowSetters = true)
    private Cell cell;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MemberCelula id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaCreada() {
        return this.fechaCreada;
    }

    public MemberCelula fechaCreada(LocalDate fechaCreada) {
        this.setFechaCreada(fechaCreada);
        return this;
    }

    public void setFechaCreada(LocalDate fechaCreada) {
        this.fechaCreada = fechaCreada;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public MemberCelula enabled(Boolean enabled) {
        this.setEnabled(enabled);
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Member getMember() {
        return this.member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public MemberCelula member(Member member) {
        this.setMember(member);
        return this;
    }

    public Cell getCell() {
        return this.cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public MemberCelula cell(Cell cell) {
        this.setCell(cell);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberCelula)) {
            return false;
        }
        return getId() != null && getId().equals(((MemberCelula) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemberCelula{" +
            "id=" + getId() +
            ", fechaCreada='" + getFechaCreada() + "'" +
            ", enabled='" + getEnabled() + "'" +
            "}";
    }
}
