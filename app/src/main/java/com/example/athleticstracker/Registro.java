package com.example.athleticstracker;

import java.io.Serializable;
import java.util.Date;

public class Registro implements Serializable, Comparable<Registro> {
    private static final long serialVersionUID = 1L;
    private long tiempo;
    private String localidad;
    private String prueba;
    private Date fecha;

    public Registro(){
        this.tiempo = 0;
        this.localidad = "";
        this.prueba = "";
        this.fecha = new Date();
    }

    public Registro(long tiempo, String localidad, String prueba, Date fecha){
        this.tiempo = tiempo;
        this.localidad = localidad;
        this.prueba = prueba;
        this.fecha = fecha;
    }


    public long getTiempo() {
        return tiempo;
    }

    public void setTiempo(long tiempo) {
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

    @Override
    public int compareTo(Registro registro) {
        if(this.tiempo > registro.getTiempo()){
            return 1;
        }
        else{
            if(this.tiempo < registro.getTiempo()){
                return -1;
            }
            else{
                return 0;
            }
        }
    }

}
