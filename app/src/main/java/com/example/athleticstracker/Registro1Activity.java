package com.example.athleticstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButtonToggleGroup;

public class Registro1Activity extends AppCompatActivity {

    private TextView textViewUsuario;
    private Button btnContinuar;
    private Bundle bundle;
    private String mailUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro1);
        obtenerDatos();
        iniciarSiguienteActivity();
    }

    private void obtenerDatos(){
        this.textViewUsuario = (TextView) findViewById(R.id.textViewUsuario);
        this.btnContinuar = (Button) findViewById(R.id.btnContinuar);
        bundle = getIntent().getExtras();
        mailUsuario = bundle.getString("mailUsuario");
        textViewUsuario.setText(mailUsuario);
    }

    private void iniciarSiguienteActivity(){
        this.btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Registro2Activity.class);
                intent.putExtra("mailUsuario",mailUsuario);
                startActivity(intent);
            }
        });
    }

}