package com.example.athleticstracker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Usuario implements Serializable {

    private String nombre;
    private String apellidos;
    private String email;
    private String sexo;
    private String rol;
    private Date fechaNac;
    private String club;
    private ArrayList<Registro> registros;

    public Usuario() {
        this.nombre = "";
        this.apellidos = "";
        this.email = "";
        this.sexo = "";
        this.rol = "";
        this.fechaNac = new Date();
        this.club = "";
        this.registros = new ArrayList<>();
    }

    public Usuario(String nombre, String apellidos, String email, String sexo, String rol, Date fechaNac, String club, ArrayList<Registro> registros) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.sexo = sexo;
        this.rol = rol;
        this.fechaNac = fechaNac;
        this.club = club;
        this.registros =registros;
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

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public ArrayList<Registro> getRegistros() {
        return registros;
    }

    public void setRegistros(ArrayList<Registro> registros) {
        this.registros = registros;
    }
}
