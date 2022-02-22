package com.example.athleticstracker;

import java.io.Serializable;
import java.util.Date;

public class Registro implements Serializable {
    private static final long serialVersionUID = 1L;
    private double tiempo;
    private String localidad;
    private String prueba;
    private Date fecha;

    public Registro(){
        this.tiempo = 0;
        this.localidad = "";
        this.prueba = "";
        this.fecha = new Date();
    }

    public Registro(double tiempo, String localidad, String prueba, Date fecha){
        this.tiempo = tiempo;
        this.localidad = localidad;
        this.prueba = prueba;
        this.fecha = fecha;
    }


    public double getTiempo() {
        return tiempo;
    }

    public void setTiempo(double tiempo) {
        this.tiempo = tiempo;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getPrueba() {
        return prueba;
    }

    public void setPrueba(String prueba) {
        this.prueba = prueba;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
