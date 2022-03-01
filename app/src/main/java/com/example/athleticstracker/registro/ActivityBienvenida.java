package com.example.athleticstracker.registro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.athleticstracker.ActivityMenuUsuario;
import com.example.athleticstracker.R;
import com.example.athleticstracker.entidades.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Clase Bienvenida que formaliza el completo de registro de usuarios en la aplicación
 */
public class ActivityBienvenida extends AppCompatActivity {

    private TextView textViewUsuario;
    private Button btnComenzar;
    private Button btnOlvidasteContrasenia;
    private Bundle bundle;
    private String mailUsuario;
    private String contrasenia;
    private Usuario usuario;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        obtenerDatos();
        iniciarSiguienteActivity();
    }

    /**
     * Obtenemos los datos del layout y recogemos los datos a través del Bundle
     */
    private void obtenerDatos() {
        this.textViewUsuario = (TextView) findViewById(R.id.textViewUsuario);
        this.btnComenzar = (Button) findViewById(R.id.btnComenzar);

        //Lo obtenemos y lo ocultamos
        this.btnOlvidasteContrasenia = (Button) findViewById(R.id.btnOlvidasteContrasenia);
        btnOlvidasteContrasenia.setVisibility(View.INVISIBLE);
        this.btnOlvidasteContrasenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                olvidarContraseña();
            }
        });

        bundle = getIntent().getExtras();
        this.mailUsuario = bundle.getString(getResources().getString(R.string.mailUsuario));
        this.contrasenia = bundle.getString(getResources().getString(R.string.contrasenia));
        this.usuario = (Usuario) bundle.get(getResources().getString(R.string.usuario));
        textViewUsuario.setText(usuario.getNombre());
    }

    /**
     * Método que nos permite enviar al usuario a través del mail indicado, la posibilidad de reestablecer la contraseña
     */
    public void olvidarContraseña() {
        FirebaseAuth.getInstance().sendPasswordResetEmail(mailUsuario)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), R.string.mensaje_enviado_mail, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Método que permite al usuario acceder a la aplicación y completar su proceso de registro,
     * realizando las comprobaciones necesarias.
     */
    private void iniciarSiguienteActivity() {
        this.btnComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Comprobamos si existe algún usuario con ese email
                mAuth.fetchSignInMethodsForEmail(mailUsuario)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                boolean noExisteMail = task.getResult().getSignInMethods().isEmpty();
                                if (noExisteMail) {
                                    mAuth.createUserWithEmailAndPassword(mailUsuario, contrasenia).addOnCompleteListener(ActivityBienvenida.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {

                                                //Al logearse correctamente, asignamos este mail tambien al usuario en la BBDD
                                                usuario.setEmail(mailUsuario);

                                                Intent intent;

                                                //Registro del usuario en la BBDD
                                                mDatabase.collection(getResources().getString(R.string.users)).document(mailUsuario).set(usuario);
                                                Toast.makeText(getApplicationContext(), R.string.registro_exito, Toast.LENGTH_SHORT).show();

                                                //Acceso a un menu
                                                intent = new Intent(getApplicationContext(), ActivityMenuUsuario.class);
                                                intent.putExtra(getResources().getString(R.string.usuario), usuario);
                                                startActivity(intent);


                                            } else {
                                                System.out.println(task.getException());
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getApplicationContext(), R.string.mail_duplicado, Toast.LENGTH_SHORT).show();
                                    btnOlvidasteContrasenia.setVisibility(View.VISIBLE);
                                }

                            }
                        });
            }
        });
    }

}