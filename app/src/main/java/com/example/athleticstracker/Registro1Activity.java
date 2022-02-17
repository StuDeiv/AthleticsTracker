package com.example.athleticstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButtonToggleGroup;

public class Registro1Activity extends AppCompatActivity {

    private TextView textViewUsuario;
    private Button btnContinuar;
    private Bundle bundle;
    private String mailUsuario;
    private String contrasenia;

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
        this.mailUsuario = bundle.getString("mailUsuario");
        this.contrasenia = bundle.getString("contrasenia");
        Toast.makeText(getApplicationContext(), contrasenia, Toast.LENGTH_SHORT).show();
        textViewUsuario.setText(mailUsuario);
    }

    private void iniciarSiguienteActivity(){
        this.btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Registro2Activity.class);
                intent.putExtra("mailUsuario", mailUsuario);
                intent.putExtra("contrasenia", contrasenia);
                startActivity(intent);
            }
        });
    }

}