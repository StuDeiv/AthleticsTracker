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
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

public class Registro2Activity extends AppCompatActivity {

    private EditText editTextNombre;
    private TextView editTextApellidos;
    private EditText editTextFechaNacimiento;
    private Spinner spinnerSexo;
    private Spinner spinnerRol;
    private Button btnSiguiente;

    private Bundle bundle;

    private LocalDate fecha;
    private String mailUsuario;
    private String contrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro2);
        iniciarDatos();

    }

    private void iniciarDatos(){
        // Recogemos los datos de la activity anterior
        bundle = getIntent().getExtras();
        this.mailUsuario = bundle.getString("mailUsuario");
        this.contrasenia = bundle.getString("contrasenia");

        //Capturamos los elementos del layout
        this.editTextNombre = (EditText) findViewById(R.id.editTextNombre);
        this.editTextApellidos = (EditText) findViewById(R.id.editTextApellidos);
        this.spinnerSexo = (Spinner) findViewById(R.id.spinnerSexo);
        this.spinnerRol = (Spinner) findViewById(R.id.spinnerRol);
        this.editTextFechaNacimiento = (EditText) findViewById(R.id.editTextFechaCelebracion);
        this.btnSiguiente = (Button) findViewById(R.id.btnSiguiente);

        //Asignamos los clicks listeners para el botón y para el EditText de la fecha
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continuarRegistro();
            }
        });
        this.editTextFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogoFecha();
            }
        });
    }

    /* Este método crea un Dialogo de selección de fecha. Le asigna el valor a una variable y además
       escribe la fecha en el EditText. */
    private void dialogoFecha(){
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                fecha = LocalDate.of(year, month, day);
                editTextFechaNacimiento.setText(day+"-"+month+"-"+year);
            }
        }, 1995, 1, 1);
        dpd.show();
    }

    /* Recogemos los valores del formulario y, previa comprobación, creamos un objeto usuario y le
       añadimos sus atributos (nombre, apellidos, etc). Creamos un intent nuevo y le pasamos los datos. */
    private void continuarRegistro() {
        String nombre;
        String apellidos;
        String rol;
        String sexo;
        Usuario usuario;
        if(verificarCampos()){
            nombre = this.editTextNombre.getText().toString();
            apellidos = this.editTextApellidos.getText().toString();
            rol = this.spinnerRol.getSelectedItem().toString();
            sexo = this.spinnerSexo.getSelectedItem().toString();
            usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setApellidos(apellidos);
            usuario.setFechaNac(this.fecha);
            usuario.setSexo(sexo);
            usuario.setRol(rol);

            Intent intent = new Intent(getApplicationContext(),Registro3Activity.class);
            intent.putExtra("mailUsuario", mailUsuario);
            intent.putExtra("contrasenia", contrasenia);
            intent.putExtra("usuario", usuario);
            startActivity(intent);
        }
    }

    /* Este método comprueba que los campos nombre, apellidos y fecha no estén vacíos. */
    private boolean verificarCampos(){
        String nombre = this.editTextNombre.getText().toString();
        String apellidos = this.editTextApellidos.getText().toString();
        if(StringUtils.isBlank(nombre)){
            Toast.makeText(this, "El campo Nombre no puede estar vacío.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(StringUtils.isBlank(apellidos)){
            Toast.makeText(this, "El campo Apellidos no puede estar vacío.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(fecha == null){
            Toast.makeText(this, "Debe seleccionar una fecha de nacimiento.", Toast.LENGTH_LONG).show();
            return false;
        }
        //Comprobamos que la fecha seleccionada no se encuentre después de la fecha actual
        if(fecha.isAfter(LocalDate.now())){
            Toast.makeText(this, "Fecha de nacimiento debe ser anterior a la fecha actual...", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}