package com.example.athleticstracker;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Usuario implements Serializable {

    private String nombre;
    private String apellidos;
    private String email;
    private String sexo;
    private String rol;
    private LocalDate fechaNac;
    private Club club;
    private ArrayList<Prueba> pruebas;

    public Usuario() {
        this.nombre = "";
        this.apellidos = "";
        this.email = "";
        this.sexo = "";
        this.rol = "";
        this.fechaNac = null;
        this.club = new Club();
        this.pruebas = new ArrayList<>();
    }

    public Usuario(String nombre, String apellidos, String email, String sexo, String rol, LocalDate fechaNac, Club club, ArrayList<Prueba> pruebas) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.sexo = sexo;
        this.rol = rol;
        this.fechaNac = fechaNac;
        this.club = club;
        this.pruebas = pruebas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public LocalDate getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(LocalDate fechaNac) {
        this.fechaNac = fechaNac;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public ArrayList<Prueba> getPruebas() {
        return pruebas;
    }

    public void setPruebas(ArrayList<Prueba> pruebas) {
        this.pruebas = pruebas;
    }
}