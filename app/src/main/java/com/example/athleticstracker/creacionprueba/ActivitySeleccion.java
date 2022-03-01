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
        setContentView(R.layout.activity_seleccion);
        iniciarDatos();

        this.btnElegirAtletas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificarCampos()){
                    //Creamos la prueba con los datos que ha introducido el usuario
                    Prueba prueba = new Prueba(
                            spinnerSeleccionPrueba.getSelectedItem().toString(),
                            editTextLocalidadPrueba.getText().toString(),
                            new Date(),
                            new HashMap<>()
                            );

                    //Enviamos los objetos a la siguiente activity
                    Intent intent = new Intent(getApplicationContext(), ActivitySeleccionAtletas.class);
                    intent.putExtra(getResources().getString(R.string.usuario),usuario);
                    intent.putExtra(getResources().getString(R.string.prueba),prueba);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    /**
     * Recogemos los datos del la activity anterior y capturamos los elementos del layout
     */
    private void iniciarDatos() {
        bundle = getIntent().getExtras();
        usuario = (Usuario) bundle.getSerializable(getResources().getString(R.string.usuario));

        editTextLocalidadPrueba = (EditText) findViewById(R.id.editTextLocalidadPrueba);
        btnElegirAtletas = (Button) findViewById(R.id.btnElegirAtletas);

        spinnerSeleccionPrueba = (Spinner) findViewById(R.id.spinnerSeleccionPrueba);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item_spinner, getResources().getStringArray(R.array.pruebas));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSeleccionPrueba.setAdapter(adapter);
    }

    /**
     * Método que verifica si los datos son válidos (una prueba seleccionada, y un nombre que no sea vacío)
     * @return true si pasa la verificación, false en caso contrario
     */
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