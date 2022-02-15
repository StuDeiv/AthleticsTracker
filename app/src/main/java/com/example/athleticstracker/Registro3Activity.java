package com.example.athleticstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class Registro3Activity extends AppCompatActivity {

    private Spinner spinnerSeleccionClub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro3);
        this.spinnerSeleccionClub = (Spinner) findViewById(R.id.spinnerSeleccionClub);
        cargarDatosSpinner();
    }

    private void cargarDatosSpinner(){
        ArrayList<String> nombreClubes = new ArrayList<>();
        nombreClubes.add("Atletismo en general");
        String[] nombreClubesArray = nombreClubes.toArray(new String[0]);
        ArrayAdapter<Object> adaptador = new ArrayAdapter<Object>(this, android.R.layout.simple_list_item_1,nombreClubesArray);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerSeleccionClub.setAdapter(adaptador);

    }
}