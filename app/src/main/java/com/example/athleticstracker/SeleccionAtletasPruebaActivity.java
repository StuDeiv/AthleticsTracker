package com.example.athleticstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SeleccionAtletasPruebaActivity extends AppCompatActivity{

    private Club club;
    private Bundle bundle;
    private Usuario usuario;
    private Prueba prueba;
    private Button btnSeleccionAtletaCalle1;
    private FirebaseFirestore mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_seleccion_atletas_prueba);
        iniciarDatos();
    }

    private void iniciarDatos() {
        bundle = getIntent().getExtras();
        mDatabase = FirebaseFirestore.getInstance();
        usuario = (Usuario) bundle.getSerializable("usuario");
        prueba = (Prueba) bundle.getSerializable("prueba");
        btnSeleccionAtletaCalle1 = (Button) findViewById(R.id.btnSeleccionAtletaCalle1);
        traspasarDatosBDaListView();

        //TODO: Pendiente de generar un AlertDialog excluyente por cada una de las calles
        this.btnSeleccionAtletaCalle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                ArrayList<String> nombresAtletas = new ArrayList<>();
                mDatabase.collection("users")
                        .whereEqualTo("club", usuario.getClub())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Usuario user = document.toObject(Usuario.class);
                                        nombresAtletas.add(usuario.getNombre()+" "+usuario.getApellidos());
                                    }
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });
                String[] nombreAtletasArray = nombresAtletas.toArray(new String[0]);
                builder.setTitle("Seleccion corredor calle 1");
                builder.setItems(nombreAtletasArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("Seleccionado"+ nombreAtletasArray[i]);
                    }
                });
                builder.show();
            }
        });

    }

    private void traspasarDatosBDaListView(){
        ArrayList<String> nombresAtletas = new ArrayList<>();
        mDatabase.collection("users")
                .whereEqualTo("club", usuario.getClub())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Usuario usuario = document.toObject(Usuario.class);


                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

}