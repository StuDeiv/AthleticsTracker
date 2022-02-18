package com.example.athleticstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthActivity extends AppCompatActivity {

    //private final String TITULO_PANTALLA = "INICIO SESIÓN";
    private EditText editTextMail;
    private EditText editTextContrasenia;
    private Button btnAcceder;
    private Button btnRegistrar;
    private FirebaseFirestore mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        inicializarComponentes();
        registroUsuarios();
        iniciarSesion();
        mDatabase = FirebaseFirestore.getInstance();
    }

    private void inicializarComponentes() {
        this.editTextMail = (EditText) findViewById(R.id.editTextTextEmail);
        this.editTextContrasenia = (EditText) findViewById(R.id.editTextTextContrasenia);
        this.btnAcceder = (Button) findViewById(R.id.btnAcceder);
        this.btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
    }

    private void registroUsuarios(){
        this.btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mailUsuario = editTextMail.getText().toString();
                String contrasenia = editTextContrasenia.getText().toString();
                Intent intent = new Intent(getApplicationContext(), BienvenidaActivity.class);
                intent.putExtra("mailUsuario",mailUsuario);
                intent.putExtra("contrasenia",contrasenia);
                startActivity(intent);
            }
        });
    }

    private void iniciarSesion(){
        this.btnAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mailUsuario = editTextMail.getText().toString();
                //Si los campos mail y contraseña no están vacios
                if (!editTextMail.getText().toString().isEmpty() && !editTextContrasenia.getText().toString().isEmpty()){

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(mailUsuario,editTextContrasenia.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                mDatabase.collection("users").document(mailUsuario).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Usuario user = documentSnapshot.toObject(Usuario.class);
                                        switch (user.getRol()){
                                            case "Atleta": intentAltleta(user);
                                                break;
                                            case "Entreandor": intentEntrenador(user);
                                                break;
                                        }
                                    }
                                });
                                Intent intent = new Intent(getApplicationContext(), BienvenidaActivity.class);
                                intent.putExtra("mailUsuario",mailUsuario);
                                startActivity(intent);
                            }else{
                                mostrarAlertaRegistroUsuario();
                            }
                        }
                    });

                }
            }
        });
    }

    private void mostrarAlertaRegistroUsuario(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error en el inicio de sesión");
        builder.setMessage("Ha habido un error al iniciar sesión con este mail y contraseña");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}