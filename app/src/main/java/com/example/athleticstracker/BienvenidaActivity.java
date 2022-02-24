package com.example.athleticstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
                                boolean existeMail = task.getResult().getSignInMethods().isEmpty();
                                if (existeMail) {
                                    Toast.makeText(getApplicationContext(),"Hola "+mailUsuario,Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(),"Este mail ya se encuentra registrado en nuestro servidor",Toast.LENGTH_SHORT).show();
                                    btnOlvidasteContrasenia.setVisibility(View.VISIBLE);
                                }

                            }
                        });
            }
        });
    }

}