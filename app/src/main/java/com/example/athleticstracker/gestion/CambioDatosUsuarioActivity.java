package com.example.athleticstracker.gestion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.athleticstracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.commons.lang3.StringUtils;

public class CambioDatosUsuarioActivity extends AppCompatActivity {

    private EditText editTextNuevoMailUsuario;
    private EditText editTextNuevaContrasenia;
    private Button btnCambiarDatosUsuario;
    private Button btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_datos_usuario);
        iniciarDatos();


        //Cambiar mail usuario y contraseña
        btnCambiarDatosUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!StringUtils.isBlank(editTextNuevoMailUsuario.getText().toString()) && !StringUtils.isBlank(editTextNuevaContrasenia.getText().toString())){
                    //Mail
                    FirebaseAuth.getInstance().getCurrentUser().updateEmail(editTextNuevoMailUsuario.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                    }
                                }
                            });
                    //Contraseña
                    FirebaseAuth.getInstance().getCurrentUser().updatePassword(editTextNuevaContrasenia.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                    }
                                }
                            });
                    //TODO: Revisar si merece la pena realizar aquí finish()
                    finish();
                }else{
                    if (editTextNuevaContrasenia.getText().toString().length() < 6){
                        Toast.makeText(getApplicationContext(),"La contraseña debe tener al menos 6 caracteres",Toast.LENGTH_SHORT).show();
                    }
                    if (StringUtils.isBlank(editTextNuevoMailUsuario.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Introduce un mail, por favor",Toast.LENGTH_SHORT).show();
                    }
                    if (StringUtils.isBlank(editTextNuevaContrasenia.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Introduce una contraseña, por favor",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void iniciarDatos() {
        editTextNuevoMailUsuario = (EditText) findViewById(R.id.editTextNuevoMailUsuario);
        editTextNuevaContrasenia = (EditText) findViewById(R.id.editTextNuevaContrasenia);
        btnCambiarDatosUsuario = (Button) findViewById(R.id.btnCambiarDatosUsuario);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
    }
}