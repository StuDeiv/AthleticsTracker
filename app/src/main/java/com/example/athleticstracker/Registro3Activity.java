package com.example.athleticstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Registro3Activity extends AppCompatActivity {

    private Spinner spinnerSeleccionClub;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro3);
        this.spinnerSeleccionClub = (Spinner) findViewById(R.id.spinnerSeleccionClub);
        cargarDatosSpinner();
    }

    private void cargarDatosSpinner(){
        ArrayList<String> nombreClubes = new ArrayList<>();
        db.collection("clubes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                nombreClubes.add((String) document.getData().get("nombre"));
                                String[] nombreClubesArray = nombreClubes.toArray(new String[0]);
                                ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,nombreClubesArray);
                                adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerSeleccionClub.setAdapter(adaptador);
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}