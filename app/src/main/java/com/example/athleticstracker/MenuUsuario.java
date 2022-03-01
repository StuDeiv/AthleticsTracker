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
import com.example.athleticstracker.gestion.AjustesUsuarioActivity;
import com.example.athleticstracker.visualizaciondatos.ComparadorActivity;
import com.example.athleticstracker.visualizaciondatos.DatosClub;
import com.example.athleticstracker.visualizaciondatos.RegistrosAtleta;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MenuUsuario extends AppCompatActivity{

    private Usuario usuario;
    private Button btnRegistros;
    private Button btnDatosClub;
    private Button btnComparador;
    private Button btnIniciarPrueba;
    private FirebaseFirestore mDatabase;

    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_menu_atleta);

        mDatabase = FirebaseFirestore.getInstance();
        this.btnRegistros = (Button) findViewById(R.id.buttonRegistrosPersonales);
        this.btnDatosClub = (Button) findViewById(R.id.btnVerClubAtleta);
        this.btnComparador = (Button) findViewById(R.id.btnComparador);
        btnIniciarPrueba = (Button) findViewById(R.id.btnIniciarPrueba);

        recuperarDatos();

        this.btnRegistros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RegistrosAtleta.class);
                intent.putExtra("registros", usuario.getRegistros());
                startActivity(intent);
            }
        });

        this.btnDatosClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.collection("clubes").document(usuario.getClub()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Club club = documentSnapshot.toObject(Club.class);
                        Intent intent = new Intent(getApplicationContext(), DatosClub.class);
                        intent.putExtra("club", club);
                        startActivity(intent);
                    }
                });
            }
        });

        this.btnComparador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ComparadorActivity.class);
                startActivity(intent);
            }
        });

        if(usuario.getRol().equals("Entrenador")){
            btnIniciarPrueba.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ActivitySeleccion.class);
                    intent.putExtra("usuario",usuario);
                    startActivity(intent);
                }
            });
        }
        else{
            this.btnIniciarPrueba.setVisibility(View.INVISIBLE);
        }

    }

    private void recuperarDatos(){
        this.usuario = (Usuario) getIntent().getExtras().getSerializable("usuario");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.perfilUsuario:
                Intent intent = new Intent(getApplicationContext(), AjustesUsuarioActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}