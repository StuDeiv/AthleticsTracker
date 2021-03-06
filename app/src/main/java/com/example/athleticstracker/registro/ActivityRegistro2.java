package com.example.athleticstracker.registro;

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

import com.example.athleticstracker.R;
import com.example.athleticstracker.entidades.Usuario;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Clase correspondiente al Paso 2 del registro, en donde se recogen los campos nombre,
 * apellidos, fecha nacimiento, sexo y rol (Entrenador/Atleta)
 */
public class ActivityRegistro2 extends AppCompatActivity {

    private EditText editTextNombre;
    private TextView editTextApellidos;
    private EditText editTextFechaNacimiento;
    private Spinner spinnerSexo;
    private Spinner spinnerRol;
    private Button btnSiguiente;

    private Bundle bundle;

    private Date fecha;
    private String mailUsuario;
    private String contrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro2);
        iniciarDatos();

    }

    /**
     * Método que obtiene los datos correspondientes del layout así como los provenientes de otras
     * activities a través del Bundle
     */
    private void iniciarDatos() {
        // Recogemos los datos de la activity anterior
        bundle = getIntent().getExtras();
        this.mailUsuario = bundle.getString(getResources().getString(R.string.mailUsuario));
        this.contrasenia = bundle.getString(getResources().getString(R.string.contrasenia));
        System.out.println(mailUsuario);

        //Capturamos los elementos del layout
        this.editTextNombre = (EditText) findViewById(R.id.editTextNombre);
        this.editTextApellidos = (EditText) findViewById(R.id.editTextLocalidad);
        this.spinnerSexo = (Spinner) findViewById(R.id.spinnerSexo);
        this.spinnerRol = (Spinner) findViewById(R.id.spinnerRol);
        this.editTextFechaNacimiento = (EditText) findViewById(R.id.editTextFechaPrueba);
        this.btnSiguiente = (Button) findViewById(R.id.btnSiguienteClub);

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

    /**
     * Este método crea un Dialogo de selección de fecha. Le asigna el valor a una variable y además
     * escribe la fecha en el EditText.
     */
    private void dialogoFecha() {
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                LocalDate localDatePicker = LocalDate.of(year, month, day);
                fecha = Date.from(localDatePicker.atStartOfDay(ZoneId.systemDefault()).toInstant());
                editTextFechaNacimiento.setText(day + "-" + month + "-" + year);
            }
        }, LocalDate.now().getYear() - 5, LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
        dpd.show();
    }

    /**
     * Recogemos los valores del formulario y, previa comprobación, creamos un objeto usuario y le
     * añadimos sus atributos (nombre, apellidos, etc). Creamos un intent nuevo y le pasamos los datos.
     */
    private void continuarRegistro() {
        String nombre;
        String apellidos;
        String rol;
        String sexo;
        Usuario usuario;
        if (verificarCampos()) {
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

            Intent intent = new Intent(getApplicationContext(), ActivityRegistro3.class);
            intent.putExtra(getResources().getString(R.string.mailUsuario), mailUsuario);
            intent.putExtra(getResources().getString(R.string.contrasenia), contrasenia);
            intent.putExtra(getResources().getString(R.string.usuario), usuario);
            startActivity(intent);
        }
    }

    /**
     * Este método comprueba que los campos nombre, apellidos y fecha no estén vacíos.
     *
     * @return Devuelve true, si todos los campos están correctamente y false, si hay algún campo erroneo
     */
    private boolean verificarCampos() {
        String nombre = this.editTextNombre.getText().toString();
        String apellidos = this.editTextApellidos.getText().toString();
        if (StringUtils.isBlank(nombre)) {
            Toast.makeText(this, R.string.mensaje_error_nombre_registro, Toast.LENGTH_LONG).show();
            return false;
        }
        if (StringUtils.isBlank(apellidos)) {
            Toast.makeText(this, R.string.mensaje_error_apellidos_registro, Toast.LENGTH_LONG).show();
            return false;
        }
        if (fecha == null) {
            Toast.makeText(this, R.string.mensaje_error_fecha_nacimiento_registro, Toast.LENGTH_LONG).show();
            return false;
        }
        //Comprobamos que la fecha seleccionada no se encuentre después de la fecha actual
        if (fecha.after(new Date())) {
            Toast.makeText(this, R.string.mensaje_error_fecha_superior_actual, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}