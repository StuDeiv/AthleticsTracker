package com.example.athleticstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Registro1Activity extends AppCompatActivity {

    private TextView textViewUsuario;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro1);
        obtenerDatos();
    }

    private void obtenerDatos(){
        this.textViewUsuario = (TextView) findViewById(R.id.textViewUsuario);
        bundle = getIntent().getExtras();
        textViewUsuario.setText(bundle.getString("mailUsuario"));
    }

}