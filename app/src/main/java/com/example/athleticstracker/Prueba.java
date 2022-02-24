package com.example.athleticstracker;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;

public class Prueba implements Serializable {

    private String tipo;
    private String localidad;
    private LocalDate fecha;
    private HashMap<String, String> mapaRegistros;

    public Prueba() {
        this.tipo = "";
        this.localidad = "";
        this.fecha = null;
        this.mapaRegistros = new HashMap<>();
    }


}
