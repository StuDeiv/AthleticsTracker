package com.example.athleticstracker;

import java.io.Serializable;
import java.util.ArrayList;

public class Club implements Serializable {
    private String nombre;
    private String localidad;
    private String mail;
    ArrayList<Prueba> lPruebas;

    public Club() {}

    public Club(String nombre, String localidad, String mail, ArrayList<Prueba> lPruebas) {
        this.nombre = nombre;
        this.localidad = localidad;
        this.mail = mail;
        this.lPruebas = lPruebas;
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

    public void setLocalidad(String localidad) {this.localidad = localidad;}

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public ArrayList<Prueba> getlPruebas() {
        return lPruebas;
    }

    public void setlPruebas(ArrayList<Prueba> lPruebas) {
        this.lPruebas = lPruebas;
    }
}
