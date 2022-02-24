package com.example.athleticstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuAtleta extends AppCompatActivity {

    private Usuario usuario;
    private Button btnRegistros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_menu_atleta);
        this.btnRegistros = (Button) findViewById(R.id.buttonRegistrosPersonales);
        recuperarDatos();
        this.btnRegistros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RegistrosAtleta.class);
                intent.putExtra("registros", usuario.getRegistros());
                startActivity(intent);
            }
        });

    }

    private void recuperarDatos(){
        this.usuario = (Usuario) getIntent().getExtras().getSerializable("user");
    }
}