package com.example.athleticstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class RegistrosAtleta extends AppCompatActivity {

    private Usuario usuario;
    private ArrayList<Registro> lRegistros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_registros_atleta);

        cargarDatos();
    }

    private void cargarDatos(){
        this.lRegistros = (ArrayList<Registro>) getIntent().getExtras().getSerializable("registros");
    }
}