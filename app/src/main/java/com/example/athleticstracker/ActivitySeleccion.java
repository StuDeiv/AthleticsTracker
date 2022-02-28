package com.example.athleticstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ActivitySeleccion extends AppCompatActivity {

    private Bundle bundle;
    private Usuario usuario;

    private Spinner spinnerSeleccionPrueba;
    private EditText editTextLocalidadPrueba;
    private Button btnElegirAtletas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_seleccion);
        iniciarDatos();

        this.btnElegirAtletas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificarCampos()){
                    //Enviamos los campos recibidos con anterioridad
                    usuario = (Usuario) bundle.getSerializable("usuario");

                    //Obtenemos los datos recogidos en esta activity relativos a la prueba

                    Prueba prueba = new Prueba(
                            spinnerSeleccionPrueba.getSelectedItem().toString(),
                            editTextLocalidadPrueba.getText().toString(),
                            new Date(),
                            new HashMap<>()
                            );

                    //Enviamos los objetos a la siguiente activity
                    Intent intent = new Intent(getApplicationContext(),SeleccionAtletasPruebaActivity.class);
                    intent.putExtra("usuario",usuario);
                    intent.putExtra("prueba",prueba);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    private void iniciarDatos() {
        bundle = getIntent().getExtras();
        spinnerSeleccionPrueba = (Spinner) findViewById(R.id.spinnerSeleccionPrueba);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_item, getResources().getStringArray(R.array.pruebas));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSeleccionPrueba.setAdapter(adapter);
        editTextLocalidadPrueba = (EditText) findViewById(R.id.editTextLocalidadPrueba);
        btnElegirAtletas = (Button) findViewById(R.id.btnElegirAtletas);
    }

    private boolean verificarCampos() {
        if (StringUtils.isBlank(spinnerSeleccionPrueba.getSelectedItem().toString())){
            Toast.makeText(getApplicationContext(),"Selecciona una prueba por favor",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtils.isBlank(editTextLocalidadPrueba.getText().toString())){
            Toast.makeText(getApplicationContext(),"Selecciona una localidad por favor",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}