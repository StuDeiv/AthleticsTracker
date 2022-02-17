package com.example.athleticstracker;

import java.io.Serializable;

public class Club implements Serializable {
    private String nombre;
    private String localidad;

    public Club() {
        this.nombre = "";
        this.localidad = "";
    }

    public Club(String nombre, String localidad) {
        this.nombre = nombre;
        this.localidad = localidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }
}
