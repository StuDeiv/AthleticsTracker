package com.example.athleticstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RegistroClubActivity extends AppCompatActivity {

    private EditText editTextNombre;
    private EditText editTextLocalidad;
    private EditText editTextMail;
    private Button btnSiguienteClub;
    private Bundle bundle;
    private String mailUsuario;
    private String contrasenia;
    private Usuario usuario;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<String> lNombreClubs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_registro_club);
        iniciarDatos();
        lNombreClubs = obtenerClubesBD();

        this.btnSiguienteClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreClubIntroducido = editTextNombre.getText().toString();
                String uidPersonalizado = nombreClubIntroducido.replaceAll("\\s+","").toLowerCase();
                if (!buscarNombreArray(nombreClubIntroducido)){
                    Club club = new Club(
                            nombreClubIntroducido,
                            editTextLocalidad.getText().toString(),
                            editTextMail.getText().toString(),
                            new ArrayList<>()
                    );
                    db.collection("clubes").document(uidPersonalizado).set(club);
                    Toast.makeText(getApplicationContext(),"Club registrado oon éxito",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Registro3Activity.class);
                    intent.putExtra("mailUsuario",mailUsuario);
                    intent.putExtra("contrasenia",contrasenia);
                    //Antes de enviarlo, asociamos el club registrado al atleta
                    usuario.setClub(uidPersonalizado);
                    intent.putExtra("usuario",usuario);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(),"Ese club ya existe",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean buscarNombreArray(String nombreClubIntroducido) {
        int i = 0;
        boolean encontrado = false;
        while (!encontrado && i < lNombreClubs.size()){
            if (nombreClubIntroducido.equals(lNombreClubs.get(i))){
                encontrado = true;
            }
            i++;
        }
        return encontrado;
    }


    private void iniciarDatos(){
        bundle = getIntent().getExtras();
        mailUsuario = bundle.getString("mailUsuario");
        contrasenia = bundle.getString("contrasenia");
        usuario = (Usuario) bundle.get("usuario");
        System.out.println(mailUsuario);
        this.editTextNombre = (EditText) findViewById(R.id.editTextNombre);
        this.editTextLocalidad = (EditText) findViewById(R.id.editTextLocalidad);
        this.editTextMail = (EditText) findViewById(R.id.editTextMail);
        this.btnSiguienteClub = (Button) findViewById(R.id.btnSiguienteClub);
    }



    /**
     * Obtenemos el listado de clubes que se encuentran en nuestra BBDD.
     * DISCLAIMER: Aunque sea poco efectivo, hemos preferido obtener el listado de clubes y obtenerlo en memoria para evitar realizar
     * operaciones de lectura en Firebase
     * @return
     */
    private ArrayList<String> obtenerClubesBD(){
        ArrayList<String> nombreClubes = new ArrayList<>();
        db.collection("clubes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                nombreClubes.add((String) document.getData().get("nombre"));
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return nombreClubes;
    }
}