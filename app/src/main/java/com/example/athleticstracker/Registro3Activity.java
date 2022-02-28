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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Registro3Activity extends AppCompatActivity {

    private Spinner spinnerSeleccionClub;
    private Button btnRegistrarClub;
    private Button btnSiguiente;
    private Bundle bundle;
    private String mailUsuario;
    private String contrasenia;
    private Usuario usuario;
    private FirebaseFirestore mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro3);

        //Recogemos los elementos del layout
        iniciarDatos();

        //Acción pulsar botón Siguiente

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asignarClubSeleccionadoSpinner();
                Intent intent = new Intent(getApplicationContext(), BienvenidaActivity.class);
                intent.putExtra("mailUsuario", mailUsuario);
                intent.putExtra("contrasenia", contrasenia);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            }
        });

        //Acción pulsar boton registrar
        btnRegistrarClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistroClubActivity.class);
                intent.putExtra("mailUsuario", mailUsuario);
                intent.putExtra("contrasenia", contrasenia);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
                finish();
            }
        });

        cargarDatosSpinner();
    }

    /**
     * Buscamos el club seleccionado en el Spinner en nuestra BBDD y lo asignamos al usuario
     */
    private void asignarClubSeleccionadoSpinner() {
        String uidPersonalizado = spinnerSeleccionClub.getSelectedItem().toString().replaceAll("\\s+", "").toLowerCase();

        //Comprobamos que se ha seleccionado un articulo. Por defecto, si no seleccionas una opción del Spinner, saldría null.
        if (uidPersonalizado.equals(null)) {
            Toast.makeText(getApplicationContext(), "Selecciona un club, por favor", Toast.LENGTH_SHORT).show();
        } else {
            usuario.setClub(uidPersonalizado);
        }
    }

    private void iniciarDatos() {
        mDatabase = FirebaseFirestore.getInstance();
        bundle = getIntent().getExtras();
        this.mailUsuario = bundle.getString("mailUsuario");
        this.contrasenia = bundle.getString("contrasenia");
        System.out.println(mailUsuario);
        usuario = (Usuario) bundle.get("usuario");
        this.spinnerSeleccionClub = (Spinner) findViewById(R.id.spinnerSeleccionClub);
        this.btnRegistrarClub = (Button) findViewById(R.id.btnRegistrarClub);
        this.btnSiguiente = (Button) findViewById(R.id.btnSiguienteClub);
        comprobarRolBotonRegistrarClub();
    }

    private void comprobarRolBotonRegistrarClub() {
        if (usuario.getRol().equals("Atleta")) {
            btnRegistrarClub.setVisibility(View.INVISIBLE);
        }
    }

    private void cargarDatosSpinner() {
        ArrayList<String> nombreClubes = new ArrayList<>();
        mDatabase.collection("clubes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                nombreClubes.add((String) document.getData().get("nombre"));
                            }
                            String[] nombreClubesArray = nombreClubes.toArray(new String[0]);
                            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, nombreClubesArray);
                            adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerSeleccionClub.setAdapter(adaptador);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}