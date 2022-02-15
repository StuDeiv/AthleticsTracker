package com.example.athleticstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Registro2Activity extends AppCompatActivity {

    private TextView textViewMailProvide;
    private TextView textViewNombre;
    private TextView textViewApellidos;
    private TextView textViewFechaNacimiento;
    private TextView textViewSexo;
    private Button btnSiguiente;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro2);
        obtenerDatos();
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Registro3Activity.class);
                startActivity(intent);
            }
        });
    }

    private void obtenerDatos(){
        bundle = getIntent().getExtras();
        String mailUsuario = bundle.getString("mailUsuario");
        this.btnSiguiente = (Button) findViewById(R.id.btnSiguiente);
    }
}