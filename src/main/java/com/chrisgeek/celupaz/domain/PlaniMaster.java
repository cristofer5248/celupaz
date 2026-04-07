package com.chrisgeek.celupaz.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A PlaniMaster.
 */
@Entity
@Table(name = "plani_master")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlaniMaster implements Serializable {

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

    @Column(name = "ofrenda")
    private Double ofrenda;

    @Column(name = "visita_cordinador")
    private Boolean visitaCordinador;

    @Column(name = "visita_tutor")
    private Boolean visitaTutor;

    @Column(name = "visita_director")
    private Boolean visitaDirector;

    @Column(name = "otra_visita")
    private Boolean otraVisita;

    @Column(name = "note")
    private String note;

    @Column(name = "doneby")
    private String doneby;

    @Column(name = "completado")
    private Boolean completado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PlaniMaster id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public PlaniMaster fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getOfrenda() {
        return this.ofrenda;
    }

    public PlaniMaster ofrenda(Double ofrenda) {
        this.setOfrenda(ofrenda);
        return this;
    }

    public void setOfrenda(Double ofrenda) {
        this.ofrenda = ofrenda;
    }

    public Boolean getVisitaCordinador() {
        return this.visitaCordinador;
    }

    public PlaniMaster visitaCordinador(Boolean visitaCordinador) {
        this.setVisitaCordinador(visitaCordinador);
        return this;
    }

    public void setVisitaCordinador(Boolean visitaCordinador) {
        this.visitaCordinador = visitaCordinador;
    }

    public Boolean getVisitaTutor() {
        return this.visitaTutor;
    }

    public PlaniMaster visitaTutor(Boolean visitaTutor) {
        this.setVisitaTutor(visitaTutor);
        return this;
    }

    public void setVisitaTutor(Boolean visitaTutor) {
        this.visitaTutor = visitaTutor;
    }

    public Boolean getVisitaDirector() {
        return this.visitaDirector;
    }

    public PlaniMaster visitaDirector(Boolean visitaDirector) {
        this.setVisitaDirector(visitaDirector);
        return this;
    }

    public void setVisitaDirector(Boolean visitaDirector) {
        this.visitaDirector = visitaDirector;
    }

    public Boolean getOtraVisita() {
        return this.otraVisita;
    }

    public PlaniMaster otraVisita(Boolean otraVisita) {
        this.setOtraVisita(otraVisita);
        return this;
    }

    public void setOtraVisita(Boolean otraVisita) {
        this.otraVisita = otraVisita;
    }

    public String getNote() {
        return this.note;
    }

    public PlaniMaster note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDoneby() {
        return this.doneby;
    }

    public PlaniMaster doneby(String doneby) {
        this.setDoneby(doneby);
        return this;
    }

    public void setDoneby(String doneby) {
        this.doneby = doneby;
    }

    public Boolean getCompletado() {
        return this.completado;
    }

    public PlaniMaster completado(Boolean completado) {
        this.setCompletado(completado);
        return this;
    }

    public void setCompletado(Boolean completado) {
        this.completado = completado;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlaniMaster)) {
            return false;
        }
        return getId() != null && getId().equals(((PlaniMaster) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlaniMaster{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", ofrenda=" + getOfrenda() +
            ", visitaCordinador='" + getVisitaCordinador() + "'" +
            ", visitaTutor='" + getVisitaTutor() + "'" +
            ", visitaDirector='" + getVisitaDirector() + "'" +
            ", otraVisita='" + getOtraVisita() + "'" +
            ", note='" + getNote() + "'" +
            ", doneby='" + getDoneby() + "'" +
            ", completado='" + getCompletado() + "'" +
            "}";
    }
}
