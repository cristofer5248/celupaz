package com.chrisgeek.celupaz.domain;

import java.time.LocalDate;

public class AttendanceDTO {

    private Long id;
    private LocalDate fecha;
    private String memberName;
    private String cellName;
    private Boolean enabled;

    public AttendanceDTO(Long id, LocalDate fecha, String memberName, String cellName, Boolean enabled) {
        this.id = id;
        this.fecha = fecha;
        this.memberName = memberName;
        this.cellName = cellName;
        this.enabled = enabled;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
