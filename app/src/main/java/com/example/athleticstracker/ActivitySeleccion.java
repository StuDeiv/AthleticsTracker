package com.example.athleticstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class ActivitySeleccion extends AppCompatActivity {

    private Bundle bundle;
    private Usuario usuario;
    private ArrayList<Usuario> listaAtletasClub;

    private Spinner spinnerSeleccionPrueba;
    private EditText editTextFechaPrueba;
    private EditText editTextLocalidadPrueba;
    private Button btnElegirAtletas;
    private Date fechaPrueba;


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
                            fechaPrueba,
                            null
                            );

                    //Enviamos los objetos a la siguiente activity
                    Intent intent = new Intent(getApplicationContext(),SeleccionAtletasPruebaActivity.class);
                    intent.putExtra("usuario",usuario);
                    intent.putExtra("prueba",prueba);
                    startActivity(intent);

                }

            }
        });

        this.editTextFechaPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogoFecha();
            }
        });
    }

    private void iniciarDatos() {
        bundle = getIntent().getExtras();
        spinnerSeleccionPrueba = (Spinner) findViewById(R.id.spinnerSeleccionPrueba);
        editTextFechaPrueba = (EditText) findViewById(R.id.editTextFechaPrueba);
        editTextLocalidadPrueba = (EditText) findViewById(R.id.editTextLocalidadPrueba);
        btnElegirAtletas = (Button) findViewById(R.id.btnElegirAtletas);
    }

    /* Este método crea un Dialogo de selección de fecha. Le asigna el valor a una variable y además
       escribe la fecha en el EditText. */
    private void dialogoFecha(){
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                LocalDate localDatePicker = LocalDate.of(year, month, day);
                fechaPrueba = Date.from(localDatePicker.atStartOfDay(ZoneId.systemDefault()).toInstant());
                editTextFechaPrueba.setText(day+"-"+month+"-"+year);
            }
        }, LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
        dpd.show();
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
        if (StringUtils.isBlank(editTextFechaPrueba.getText().toString())){
            Toast.makeText(getApplicationContext(),"Selecciona una fecha para la prueba por favor",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}