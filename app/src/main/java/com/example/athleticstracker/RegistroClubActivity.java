package com.example.athleticstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RegistroClubActivity extends AppCompatActivity {

    private EditText editTextNombre;
    private EditText editTextLocalidad;
    private EditText editTextMail;
    private Button btnSiguienteClub;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_registro_club);
        obtenerComponentes();
    }

    private void obtenerComponentes(){
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