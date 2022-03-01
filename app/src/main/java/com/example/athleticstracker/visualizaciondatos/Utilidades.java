package com.example.athleticstracker.visualizaciondatos;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilidades {

    /**
     * Método que convierte el tiempo de la prueba a String y formatea el tiempo que se le pasa por parámetro
     *
     * @param tiempo Tiempo que quiere formatearse y convertirlo a cadena
     * @return String, cadena de texto con el tiempo formateado
     */
    public static String tiempoToString(long tiempo) {
        DecimalFormat fS = new DecimalFormat("00");
        long decimas = (tiempo % 1000) / 100;
        long segundos = tiempo / 1000;
        long minutos = (segundos / 60);
        segundos = segundos % 60;
        return String.valueOf(minutos) + ":" + fS.format(segundos) + ":" + String.valueOf(decimas);
    }

    /**
     * Método que convierte la fecha que pasa por parametro a String y formatea el tiempo que se le pasa por parámetro
     *
     * @param date Tiempo que quiere formatearse y convertirlo a cadena
     * @return String, cadena de texto con el tiempo formateado
     */
    public static String fechaToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        return dateFormat.format(date);
    }
}
