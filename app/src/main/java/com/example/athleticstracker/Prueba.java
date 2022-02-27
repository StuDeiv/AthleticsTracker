package com.example.athleticstracker;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;

public class Prueba implements Serializable{

    private String tipo;
    private String localidad;
    private Date fecha;
    private LinkedHashMap<String, Long> mapaTiempos;

    public Prueba() {
        this.tipo = "";
        this.localidad = "";
        this.fecha = null;
        this.mapaTiempos = new LinkedHashMap<>();
    }

    public Prueba(String tipo, String localidad, Date fecha, LinkedHashMap<String, Long> mapaTiempos) {
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

    public LinkedHashMap<String, Long> getMapaTiempos() {
        return mapaTiempos;
    }

    public void setMapaTiempos(LinkedHashMap<String, Long> mapaRegistros) {
        this.mapaTiempos = mapaRegistros;
    }
}
