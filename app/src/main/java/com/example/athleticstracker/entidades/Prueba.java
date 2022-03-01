package com.example.athleticstracker.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class Prueba implements Serializable{

    private String tipo;
    private String localidad;
    private Date fecha;
    private HashMap<String, Long> mapaTiempos;

    public Prueba() {
        this.tipo = "";
        this.localidad = "";
        this.fecha = null;
        this.mapaTiempos = new HashMap<>();
    }

    public Prueba(String tipo, String localidad, Date fecha, HashMap<String, Long> mapaTiempos) {
        this.tipo = tipo;
        this.localidad = localidad;
        this.fecha = fecha;
        this.mapaTiempos = mapaTiempos;
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

    public HashMap<String, Long> getMapaTiempos() {
        return mapaTiempos;
    }

    public void setMapaTiempos(HashMap<String, Long> mapaRegistros) {
        this.mapaTiempos = mapaRegistros;
    }
}
