package com.example.athleticstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;

public class BienvenidaActivity extends AppCompatActivity {

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

    private void obtenerDatos() {
        this.textViewUsuario = (TextView) findViewById(R.id.textViewUsuario);
        this.btnComenzar = (Button) findViewById(R.id.btnComenzar);

        //Lo obtenemos y lo ocultamos
        this.btnOlvidasteContrasenia = (Button) findViewById(R.id.btnOlvidasteContrasenia);
        btnOlvidasteContrasenia.setVisibility(View.INVISIBLE);

        bundle = getIntent().getExtras();
        this.mailUsuario = bundle.getString("mailUsuario");
        this.contrasenia = bundle.getString("contrasenia");
        this.usuario = (Usuario) bundle.get("usuario");
        textViewUsuario.setText(usuario.getNombre());
    }

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
                                    mAuth.createUserWithEmailAndPassword(mailUsuario, contrasenia).addOnCompleteListener(BienvenidaActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {

                                                //Al logearse correctamente, asignamos este mail tambien al usuario en la BBDD
                                                usuario.setEmail(mailUsuario);

                                                Intent intent;

                                                //Registro del usuario en la BBDD
                                                mDatabase.collection("users").document(mailUsuario).set(usuario);
                                                Toast.makeText(getApplicationContext(), "Registro completado con éxito", Toast.LENGTH_SHORT).show();

                                                //Acceso a un layout dependiendo del rol del usuario

                                                switch (usuario.getRol()) {
                                                    case "Atleta":
                                                        intent = new Intent(getApplicationContext(), MenuAtleta.class);
                                                        intent.putExtra("usuario", usuario);
                                                        startActivity(intent);
                                                        break;
                                                    case "Entrenador":
                                                        intent = new Intent(getApplicationContext(), MenuEntrenador.class);
                                                        intent.putExtra("usuario", usuario);
                                                        startActivity(intent);
                                                        break;
                                                }

                                            } else {
                                                System.out.println(task.getException());
                                            }
                                        }
                                    });
//                                    mAuth.createUserWithEmailAndPassword(mailUsuario, contrasenia)
//                                            .addOnCompleteListener(BienvenidaActivity.this, new OnCompleteListener<AuthResult>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<AuthResult> task) {
//
//
//                                                }
//                                            });
                                } else {
                                    Toast.makeText(getApplicationContext(), "Este mail ya se encuentra registrado en nuestro servidor", Toast.LENGTH_SHORT).show();
                                    btnOlvidasteContrasenia.setVisibility(View.VISIBLE);
                                }

                            }
                        });
            }
        });
    }

}