package com.openmatch.banco.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para alta y modificación de entidad bancaria.
 */
public class BancoRequest {

    @NotBlank(message = "El código es obligatorio")
    @Size(min = 1, max = 20)
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 1, max = 200)
    private String nombre;

    @Size(max = 100)
    private String pais;

    private boolean activo = true;

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
}
