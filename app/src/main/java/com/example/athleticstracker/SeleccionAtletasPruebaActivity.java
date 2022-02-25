package com.example.athleticstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SeleccionAtletasPruebaActivity extends AppCompatActivity {

    private Club club;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_seleccion_atletas_prueba);
        iniciarDatos();
    }

    private void iniciarDatos() {
        bundle = getIntent().getExtras();

    }
}