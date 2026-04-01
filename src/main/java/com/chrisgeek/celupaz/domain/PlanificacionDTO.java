package com.chrisgeek.celupaz.domain;

import java.time.LocalDate;

public class PlanificacionDTO {

    private Long id;
    private LocalDate fecha;
    private Long almaHistoryId; // Este es el que falta para el link
    private String almaName;
    private String privilegeName;

    public PlanificacionDTO(Long id, LocalDate fecha, Long almaHistoryId, String almaName, String privilegeName) {
        this.id = id;
        this.fecha = fecha;
        this.almaHistoryId = almaHistoryId;
        this.almaName = almaName;
        this.privilegeName = privilegeName;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public String getAlmaName() {
        return almaName;
    }

    public void setAlmaName(String almaName) {
        this.almaName = almaName;
    }

    public Long getAlmaHistoryId() {
        return almaHistoryId;
    }

    public void setAlmaHistoryId(Long almaHistoryId) {
        this.almaHistoryId = almaHistoryId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
