package com.openmatch.banco.dto;

import java.time.Instant;

/**
 * DTO de respuesta para entidad bancaria.
 */
public class BancoResponse {

    private Long id;
    private String codigo;
    private String nombre;
    private String pais;
    private boolean activo;
    private Instant fechaCreacion;

    public BancoResponse() {
    }

    public BancoResponse(Long id, String codigo, String nombre, String pais, boolean activo, Instant fechaCreacion) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.pais = pais;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
