package com.chrisgeek.celupaz.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Member.
 */
@Entity
@Table(name = "member")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Member implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "department")
    private String department;

    @Column(name = "municipality")
    private String municipality;

    @Column(name = "colony")
    private String colony;

    @Column(name = "is_compaz")
    private Boolean isCompaz;

    @Column(name = "fechacumple")
    private LocalDate fechacumple;

    @Column(name = "padre")
    private Boolean padre;

    @Column(name = "relacion")
    private String relacion;

    @ManyToOne(fetch = FetchType.LAZY)
    private Iglesia iglesia;

    @Size(max = 20)
    @Column(name = "created_by", length = 20)
    private String createdBy;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Member id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Member name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public Member email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public Member phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return this.department;
    }

    public Member department(String department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMunicipality() {
        return this.municipality;
    }

    public Member municipality(String municipality) {
        this.setMunicipality(municipality);
        return this;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getColony() {
        return this.colony;
    }

    public Member colony(String colony) {
        this.setColony(colony);
        return this;
    }

    public void setColony(String colony) {
        this.colony = colony;
    }

    public Boolean getIsCompaz() {
        return this.isCompaz;
    }

    public Member isCompaz(Boolean isCompaz) {
        this.setIsCompaz(isCompaz);
        return this;
    }

    public void setIsCompaz(Boolean isCompaz) {
        this.isCompaz = isCompaz;
    }

    public LocalDate getFechacumple() {
        return this.fechacumple;
    }

    public Member fechacumple(LocalDate fechacumple) {
        this.setFechacumple(fechacumple);
        return this;
    }

    public void setFechacumple(LocalDate fechacumple) {
        this.fechacumple = fechacumple;
    }

    public Boolean getPadre() {
        return this.padre;
    }

    public Member padre(Boolean padre) {
        this.setPadre(padre);
        return this;
    }

    public void setPadre(Boolean padre) {
        this.padre = padre;
    }

    public String getRelacion() {
        return this.relacion;
    }

    public Member relacion(String relacion) {
        this.setRelacion(relacion);
        return this;
    }

    public void setRelacion(String relacion) {
        this.relacion = relacion;
    }

    public Iglesia getIglesia() {
        return this.iglesia;
    }

    public void setIglesia(Iglesia iglesia) {
        this.iglesia = iglesia;
    }

    public Member iglesia(Iglesia iglesia) {
        this.setIglesia(iglesia);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member)) {
            return false;
        }
        return getId() != null && getId().equals(((Member) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Member{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", department='" + getDepartment() + "'" +
            ", municipality='" + getMunicipality() + "'" +
            ", colony='" + getColony() + "'" +
            ", isCompaz='" + getIsCompaz() + "'" +
            ", fechacumple='" + getFechacumple() + "'" +
            ", padre='" + getPadre() + "'" +
            ", relacion='" + getRelacion() + "'" +
            "}";
    }
}
