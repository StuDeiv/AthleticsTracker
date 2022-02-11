package com.example.athleticstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Registro2Activity extends AppCompatActivity {

    private TextView textViewMailProvide;
    private TextView textViewNombre;
    private TextView textViewApellidos;
    private TextView textViewFechaNacimiento;
    private TextView textViewSexo;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro2);
        obtenerDatos();
    }

    private void obtenerDatos(){
        bundle = getIntent().getExtras();
        String mailUsuario = bundle.getString("mailUsuario");
    }
}