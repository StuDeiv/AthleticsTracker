package com.example.athleticstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;

public class MenuAtleta extends AppCompatActivity {

    private Usuario usuario;
    private Button btnRegistros;
    private Button btnDatosClub;
    private FirebaseFirestore mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_menu_atleta);

        mDatabase = FirebaseFirestore.getInstance();

        this.btnRegistros = (Button) findViewById(R.id.buttonRegistrosPersonales);
        this.btnDatosClub = (Button) findViewById(R.id.btnVerClubAtleta);

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
                /*Prueba prueba = new Prueba();
                prueba.setTipo("100m");
                prueba.setFecha(new Date());
                prueba.setLocalidad("Plasencia");
                HashMap<String, Long> mapaRegistros = new HashMap<>();
                mapaRegistros.put("Jorge", new Long(12000));
                mapaRegistros.put("David", new Long(13000));
                prueba.setMapaRegistros(mapaRegistros);
                mDatabase.collection("clubes").document(usuario.getClub()).update("lPruebas", FieldValue.arrayUnion(prueba));*/

            }
        });

    }

    private void recuperarDatos(){
        this.usuario = (Usuario) getIntent().getExtras().getSerializable("usuario");
    }
}