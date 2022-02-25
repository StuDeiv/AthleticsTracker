package com.example.athleticstracker;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class Prueba implements Serializable{

    private String tipo;
    private String localidad;
    private Date fecha;
    private HashMap<String, String> mapaRegistros;

    public Prueba() {
        this.tipo = "";
        this.localidad = "";
        this.fecha = null;
        this.mapaRegistros = new HashMap<>();
    }

    public Prueba(String tipo, String localidad, Date fecha, HashMap<String, String> mapaRegistros) {
        this.tipo = tipo;
        this.localidad = localidad;
        this.fecha = fecha;
        this.mapaRegistros = mapaRegistros;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public HashMap<String, String> getMapaRegistros() {
        return mapaRegistros;
    }

    public void setMapaRegistros(HashMap<String, String> mapaRegistros) {
        this.mapaRegistros = mapaRegistros;
    }
}
