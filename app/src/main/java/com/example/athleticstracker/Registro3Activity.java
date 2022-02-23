package com.example.athleticstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Registro3Activity extends AppCompatActivity {

    private Spinner spinnerSeleccionClub;
    private Button btnRegistrarClub;
    private Button btnSiguiente;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro3);

        //Recogemos los elementos del layout
        this.spinnerSeleccionClub = (Spinner) findViewById(R.id.spinnerSeleccionClub);
        this.btnRegistrarClub = (Button) findViewById(R.id.btnRegistrarClub);
        this.btnSiguiente = (Button) findViewById(R.id.btnSiguienteClub);

        //Acción pulsar boton registrar
        btnRegistrarClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RegistroClubActivity.class);
                startActivity(intent);
            }
        });

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