package com.example.athleticstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuEntrenador extends AppCompatActivity {

    private Button btnIniciarPrueba;
    private Bundle bundle;
    private Usuario usuario;
    private FirebaseFirestore mDatabase;
    private Map<String, Object> hashMapDocumentUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_menu_entrenador);
        iniciarDatos();
        btnIniciarPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Usuario> lUsuariosClub = new ArrayList<>();
                mDatabase.collection("users")
                        .whereEqualTo("club", usuario.getClub())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Usuario usuario = document.toObject(Usuario.class);
                                        lUsuariosClub.add(usuario);
                                        Intent intent = new Intent(getApplicationContext(),SeleccionAtletasPruebaActivity.class);
                                        intent.putExtra("listaUsuarios",lUsuariosClub);
                                    }
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });

    }

    private void iniciarDatos() {
        bundle = getIntent().getExtras();
        usuario = (Usuario) bundle.getSerializable("usuario");
        btnIniciarPrueba = (Button) findViewById(R.id.btnIniciarPrueba);
        mDatabase = FirebaseFirestore.getInstance();
    }


}