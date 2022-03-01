package com.example.athleticstracker.registro;

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

import com.example.athleticstracker.R;
import com.example.athleticstracker.entidades.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Clase correspondiente al paso 3 del registro donde se realiza la selección o creación del Club por parte del Usuario.
 * En el caso de crear un Club, solo está habilitado para Usuarios con el rol Entrenador.
 */
public class ActivityRegistro3 extends AppCompatActivity {

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
                Intent intent = new Intent(getApplicationContext(), ActivityBienvenida.class);
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
                Intent intent = new Intent(getApplicationContext(), ActivityRegistroClub.class);
                intent.putExtra(getResources().getString(R.string.mailUsuario), mailUsuario);
                intent.putExtra(getResources().getString(R.string.contrasenia), contrasenia);
                intent.putExtra(getResources().getString(R.string.usuario), usuario);
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
            Toast.makeText(getApplicationContext(), R.string.mensaje_error_club_no_seleccionado_spinner_registro, Toast.LENGTH_SHORT).show();
        } else {
            usuario.setClub(uidPersonalizado);
        }
    }

    /**
     * Método que obtiene los datos correspondientes del layout así como los provenientes de otras
     * activities a través del Bundle
     */
    private void iniciarDatos() {
        mDatabase = FirebaseFirestore.getInstance();
        bundle = getIntent().getExtras();
        this.mailUsuario = bundle.getString(getResources().getString(R.string.mailUsuario));
        this.contrasenia = bundle.getString(getResources().getString(R.string.contrasenia));
        System.out.println(mailUsuario);
        usuario = (Usuario) bundle.get(getResources().getString(R.string.usuario));
        this.spinnerSeleccionClub = (Spinner) findViewById(R.id.spinnerSeleccionClub);
        this.btnRegistrarClub = (Button) findViewById(R.id.btnRegistrarClub);
        this.btnSiguiente = (Button) findViewById(R.id.btnSiguienteClub);
        comprobarRolBotonRegistrarClub();
    }

    /**
     * Comprueba el rol que se encuentra registrado el atleta para mostrar la opción de registrar club
     */
    private void comprobarRolBotonRegistrarClub() {
        if (usuario.getRol().equals(getResources().getString(R.string.atleta))) {
            btnRegistrarClub.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Obtenemos los clubes que están registrados en nuestra BD y los mostramos en el Spinner para que el
     * Usuario pueda seleccionarlo.
     */
    private void cargarDatosSpinner() {
        ArrayList<String> nombreClubes = new ArrayList<>();
        mDatabase.collection(getResources().getString(R.string.clubes))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                nombreClubes.add((String) document.getData().get(getResources().getString(R.string.nombre).toLowerCase()));
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