package com.example.athleticstracker;

import java.io.Serializable;
import java.util.Date;

public class Registro implements Serializable {
    private static final long serialVersionUID = 1L;
    private String tiempo;
    private String localidad;
    private Date fecha;

    public Registro(){
        this.tiempo = "";
        this.localidad = "";
        this.fecha = new Date();
    }

    public Registro(String tiempo, String localidad, Date fecha){
        this.tiempo = tiempo;
        this.localidad = localidad;
        this.fecha = fecha;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
