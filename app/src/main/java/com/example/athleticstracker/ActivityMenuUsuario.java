package com.example.athleticstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.athleticstracker.creacionprueba.ActivitySeleccion;
import com.example.athleticstracker.entidades.Club;
import com.example.athleticstracker.entidades.Usuario;
import com.example.athleticstracker.gestion.ActivityAjustesUsuario;
import com.example.athleticstracker.visualizaciondatos.ActivityComparador;
import com.example.athleticstracker.visualizaciondatos.ActivityDatosClub;
import com.example.athleticstracker.visualizaciondatos.ActivityRegistrosAtleta;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Clase que muestra el menú principal de nuestra aplicación así como el
 * manejo de las acciones dentro del menú
 */
public class ActivityMenuUsuario extends AppCompatActivity {

    private Usuario usuario;
    private Button btnRegistros;
    private Button btnDatosClub;
    private Button btnComparador;
    private Button btnIniciarPrueba;
    private FirebaseFirestore mDatabase;
    private Bundle bundle;


    @Override
    public void onBackPressed() {
        /*
        En el caso de volver hacia atrás, evitamos que el usuario vuelva a realizar el registro, cerrando así su sesión
        y volviendo a la activity de registro y/o acceso
         */
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), ActivityAuth.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_usuario);
        bundle = getIntent().getExtras();
        mDatabase = FirebaseFirestore.getInstance();
        this.btnRegistros = (Button) findViewById(R.id.buttonRegistrosPersonales);
        this.btnDatosClub = (Button) findViewById(R.id.btnVerClubAtleta);
        this.btnComparador = (Button) findViewById(R.id.btnComparador);
        btnIniciarPrueba = (Button) findViewById(R.id.btnIniciarPrueba);

        recuperarDatos();

        this.btnRegistros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Refrescamos el usuario */
                mDatabase = FirebaseFirestore.getInstance();
                mDatabase.collection(getResources().getString(R.string.users)).document(usuario.getEmail().toLowerCase()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        usuario = documentSnapshot.toObject(Usuario.class);
                        Intent intent = new Intent(getBaseContext(), ActivityRegistrosAtleta.class);
                        intent.putExtra(getResources().getString(R.string.registros), usuario.getRegistros());
                        startActivity(intent);
                    }
                });

            }
        });

        this.btnDatosClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.collection(getResources().getString(R.string.clubes)).document(usuario.getClub()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Club club = documentSnapshot.toObject(Club.class);
                        Intent intent = new Intent(getApplicationContext(), ActivityDatosClub.class);
                        intent.putExtra(getResources().getString(R.string.club), club);
                        startActivity(intent);
                    }
                });
            }
        });

        this.btnComparador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityComparador.class);
                startActivity(intent);
            }
        });

        if (usuario.getRol().equals(getResources().getString(R.string.entrenador))) {
            btnIniciarPrueba.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ActivitySeleccion.class);
                    intent.putExtra(getResources().getString(R.string.usuario), usuario);
                    startActivity(intent);
                }
            });
        } else {
            this.btnIniciarPrueba.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * Recuperamos los datos procedentes de la activity origen
     */
    private void recuperarDatos() {
        this.usuario = (Usuario) bundle.getSerializable(getResources().getString(R.string.usuario));
    }

    /**
     * Método que nos permite mostrar un ActionBar con acceso a las configuraciones del usuario
     * @param menu Menú a mostrar
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    /**
     * Método que controla las opciones de nuestro ActionBar en función del item pulsado
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.perfilUsuario:
                Intent intent = new Intent(getApplicationContext(), ActivityAjustesUsuario.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}