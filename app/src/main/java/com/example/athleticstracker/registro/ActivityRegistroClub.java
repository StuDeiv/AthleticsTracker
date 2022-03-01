package com.example.athleticstracker.registro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.athleticstracker.R;
import com.example.athleticstracker.entidades.Club;
import com.example.athleticstracker.entidades.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Clase que permite registrar un Club a través de un Usuario con rol Entrenador.
 * Para ello, simplemente se le solicitan al usuario tres campos nombre, localidad y mail.
 */
public class ActivityRegistroClub extends AppCompatActivity {

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
        setContentView(R.layout.activity_registro_club);
        iniciarDatos();
        lNombreClubs = obtenerClubesBD();

        this.btnSiguienteClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreClubIntroducido = editTextNombre.getText().toString();

                //Generamos un UID personalizado para que sea más fácil reconocerlo en la base de datos

                String uidPersonalizado = nombreClubIntroducido.replaceAll("\\s+", "").toLowerCase();

                //Comprobamos que no existe ese club en la base de datos y lo registramos

                if (!buscarNombreArray(nombreClubIntroducido)) {
                    Club club = new Club(
                            nombreClubIntroducido,
                            editTextLocalidad.getText().toString(),
                            editTextMail.getText().toString(),
                            new ArrayList<>()
                    );

                    //Acceso a la base de datos
                    db.collection(getResources().getString(R.string.clubes)).document(uidPersonalizado).set(club);
                    Toast.makeText(getApplicationContext(), R.string.mensaje_registro_club_exito, Toast.LENGTH_SHORT).show();

                    //Volvemos al paso 3 del registro
                    Intent intent = new Intent(getApplicationContext(), ActivityRegistro3.class);

                    intent.putExtra(getResources().getString(R.string.mailUsuario), mailUsuario);
                    intent.putExtra(getResources().getString(R.string.contrasenia), contrasenia);

                    //Antes de enviarlo, asociamos el club registrado al atleta
                    usuario.setClub(uidPersonalizado);
                    intent.putExtra(getResources().getString(R.string.usuario), usuario);

                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), R.string.mensaje_error_club_duplicado, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * Busca si el nombre del club introducido se encuentra entre los clubes ya registrados
     *
     * @param nombreClubIntroducido Nombre del club introducido
     * @return Devuelve true, si está presente en el listado y false, si no lo está.
     */
    private boolean buscarNombreArray(String nombreClubIntroducido) {
        int i = 0;
        boolean encontrado = false;
        while (!encontrado && i < lNombreClubs.size()) {
            if (nombreClubIntroducido.equals(lNombreClubs.get(i))) {
                encontrado = true;
            }
            i++;
        }
        return encontrado;
    }

    /**
     * Método que obtiene los datos correspondientes del layout así como los provenientes de otras
     * activities a través del Bundle
     */
    private void iniciarDatos() {
        bundle = getIntent().getExtras();
        mailUsuario = bundle.getString(getResources().getString(R.string.mailUsuario));
        contrasenia = bundle.getString(getResources().getString(R.string.contrasenia));
        usuario = (Usuario) bundle.get(getResources().getString(R.string.usuario));
        this.editTextNombre = (EditText) findViewById(R.id.editTextNombre);
        this.editTextLocalidad = (EditText) findViewById(R.id.editTextLocalidad);
        this.editTextMail = (EditText) findViewById(R.id.editTextFechaPrueba);
        this.btnSiguienteClub = (Button) findViewById(R.id.btnSiguienteClub);
    }


    /**
     * Obtenemos el listado de clubes que se encuentran en nuestra BBDD.
     * DISCLAIMER: Aunque sea poco efectivo, hemos preferido obtener el listado de clubes y obtenerlo en memoria para evitar realizar
     * operaciones de lectura en Firebase
     *
     * @return
     */
    private ArrayList<String> obtenerClubesBD() {
        ArrayList<String> nombreClubes = new ArrayList<>();
        db.collection(getResources().getString(R.string.clubes))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                nombreClubes.add((String) document.getData().get(getResources().getString(R.string.nombre)));
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return nombreClubes;
    }
}