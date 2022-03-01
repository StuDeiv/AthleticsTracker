package com.example.athleticstracker.creacionprueba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.athleticstracker.R;
import com.example.athleticstracker.entidades.Prueba;
import com.example.athleticstracker.entidades.Usuario;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;

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
            Toast.makeText(getApplicationContext(),R.string.mensaje_error_spinner_seleccion_prueba,Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtils.isBlank(editTextLocalidadPrueba.getText().toString())){
            Toast.makeText(getApplicationContext(),R.string.mensaje_error_edit_text_localidad_prueba,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}