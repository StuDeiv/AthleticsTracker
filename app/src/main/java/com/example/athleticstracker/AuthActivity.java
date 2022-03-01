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
import android.widget.Toast;
import com.example.athleticstracker.entidades.Usuario;
import com.example.athleticstracker.registro.Registro2Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.commons.lang3.StringUtils;

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
                    if (!StringUtils.isBlank(editTextMail.getText().toString()) && !StringUtils.isBlank(editTextContrasenia.getText().toString()) && editTextContrasenia.getText().toString().length() >= 6){
                        String mailUsuario = editTextMail.getText().toString();
                        String contrasenia = editTextContrasenia.getText().toString();
                        Intent intent = new Intent(getApplicationContext(), Registro2Activity.class);
                        intent.putExtra("mailUsuario",mailUsuario.toLowerCase());
                        intent.putExtra("contrasenia",contrasenia);
                        startActivity(intent);
                    }else{
                        //Toast que aparece si los campos mail y contraseña están vacíos
                        if (StringUtils.isBlank(editTextMail.getText().toString()) && StringUtils.isBlank(editTextContrasenia.getText().toString())){
                            Toast.makeText(getBaseContext(), "Campos no pueden estar vacíos", Toast.LENGTH_LONG).show();
                        }
                        //Toast que aparece si la contraseña tiene menos de 6 caracteres. Problema Firebase
                        if (editTextContrasenia.getText().toString().length() < 6){
                            Toast.makeText(getBaseContext(), "La contraseña debe tener 6 caracteres como mínimo.\n", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        });
    }

    private void iniciarSesion(){
        this.btnAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mailUsuario = editTextMail.getText().toString();
                //Si los campos mail y contraseña no están vacios
                if (!StringUtils.isBlank(editTextMail.getText().toString()) && !StringUtils.isBlank(editTextContrasenia.getText().toString())){
                    //Tratamos de iniciar sesión
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(mailUsuario,editTextContrasenia.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                //Si la autentificación se ha realizado correctamente, nos traemos de la BBDD el usuario con ese email
                                mDatabase.collection("users").document(mailUsuario.toLowerCase()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Usuario user = documentSnapshot.toObject(Usuario.class);
                                        Intent intent= new Intent(getApplicationContext(), MenuUsuario.class);
                                        intent.putExtra("usuario", user);
                                        startActivity(intent);
                                    }
                                });
                            }else{
                                mostrarAlertaRegistroUsuario();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getBaseContext(), "Campos no pueden estar vacíos", Toast.LENGTH_LONG).show();
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