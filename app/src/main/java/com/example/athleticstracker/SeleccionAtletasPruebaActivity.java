package com.example.athleticstracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SeleccionAtletasPruebaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Club club;
    private Bundle bundle;
    private Usuario usuario;
    private Prueba prueba;
    private ArrayList<Usuario> listaAtletasClubBD;
    private Button btnComenzarPrueba;
    private Button btnImportarCorredores;
    private Spinner spinnerCalle1;
    private Spinner spinnerCalle2;
    private Spinner spinnerCalle3;
    private Spinner spinnerCalle4;
    private Spinner spinnerCalle5;
    private Spinner spinnerCalle6;
    private Spinner spinnerCalle7;
    private int[] vItemsSeleccionados;
    private Usuario[] vCorredores;
    private FirebaseFirestore mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_seleccion_atletas_prueba);
        iniciarDatos();

        this.btnImportarCorredores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inicializarSpinners();
            }
        });

        inicializarListenerSpinners();

        this.btnComenzarPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprobarEstadoCalles();
                Intent intent = new Intent(getApplicationContext(),ActivityCrono.class);
                intent.putExtra("usuario",usuario);
                intent.putExtra("prueba",prueba);
                intent.putExtra("vCorredores",vCorredores);
                startActivity(intent);
                finish();
            }
        });

    }

    private void comprobarEstadoCalles() {

        //TODO: CORREGIR IMPORTACIÓN DATOS ACORDE AL ITEM SELECCIONADO

        for (int i = 0; i < vItemsSeleccionados.length; i++) {
            switch (vItemsSeleccionados[i]) {
                case 0:
                    vCorredores[i] = null;
                    break;
                default:
                    vCorredores[i] = listaAtletasClubBD.get(vItemsSeleccionados[i] - 1);
                    break;
            }
        }




        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();


    }

    private void inicializarListenerSpinners() {
        spinnerCalle1.setOnItemSelectedListener(this);
        spinnerCalle2.setOnItemSelectedListener(this);
        spinnerCalle3.setOnItemSelectedListener(this);
        spinnerCalle4.setOnItemSelectedListener(this);
        spinnerCalle5.setOnItemSelectedListener(this);
        spinnerCalle6.setOnItemSelectedListener(this);
        spinnerCalle7.setOnItemSelectedListener(this);
    }

    private void iniciarDatos() {
        //Inicializamos componentes layout
        btnComenzarPrueba = (Button) findViewById(R.id.btnComenzarPrueba);
        btnImportarCorredores = (Button) findViewById(R.id.btnImportarCorredores);
        spinnerCalle1 = (Spinner) findViewById(R.id.spinnerCalle1);
        spinnerCalle2 = (Spinner) findViewById(R.id.spinnerCalle2);
        spinnerCalle3 = (Spinner) findViewById(R.id.spinnerCalle3);
        spinnerCalle4 = (Spinner) findViewById(R.id.spinnerCalle4);
        spinnerCalle5 = (Spinner) findViewById(R.id.spinnerCalle5);
        spinnerCalle6 = (Spinner) findViewById(R.id.spinnerCalle6);
        spinnerCalle7 = (Spinner) findViewById(R.id.spinnerCalle7);

        //Iniciamos componentes extras
        listaAtletasClubBD = new ArrayList<>();
        vItemsSeleccionados = new int[7];
        vCorredores = new Usuario[7];
        bundle = getIntent().getExtras();
        mDatabase = FirebaseFirestore.getInstance();
        usuario = (Usuario) bundle.getSerializable("usuario");
        prueba = (Prueba) bundle.getSerializable("prueba");
        obtenerDatosAtletasBDporClub();
    }

    private void inicializarSpinners() {
        //Obtenemos los datos que nos interesan y los pasamos a un ArrayList

        ArrayList<String> nombreAtletas = new ArrayList<>();

        //Añadimos la opción de que la calle se encuentre vacía por defecto

        nombreAtletas.add("( Calle vacía )");

        //Añadimos el listado de atletas

        for (int i = 0; i < listaAtletasClubBD.size(); i++) {
            nombreAtletas.add(listaAtletasClubBD.get(i).getNombre() + " " + listaAtletasClubBD.get(i).getApellidos());
        }

        //Pasamos a Array de String todos esos datos del ArrayList para que funcionen los Spinners
        String[] nombreAtletasArrayAdapter = nombreAtletas.toArray(new String[0]);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, nombreAtletasArrayAdapter) {
            @Override
            public boolean isEnabled(int position) {
                for (int i = 0; i < vItemsSeleccionados.length; i++) {
                    if (position == vItemsSeleccionados[i]) {
                        return false;
                    } else {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;

                for (int i = 0; i < vItemsSeleccionados.length; i++) {
                    if (position == vItemsSeleccionados[i]) {
                        textView.setTextColor(Color.GRAY);
                    } else {
                        textView.setTextColor(Color.BLACK);
                    }
                }
                return view;
            }
        };
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //Establecemos los spinners de cada una de las calles

        spinnerCalle1.setAdapter(adaptador);
        spinnerCalle2.setAdapter(adaptador);
        spinnerCalle3.setAdapter(adaptador);
        spinnerCalle4.setAdapter(adaptador);
        spinnerCalle5.setAdapter(adaptador);
        spinnerCalle6.setAdapter(adaptador);
        spinnerCalle7.setAdapter(adaptador);
    }

    private void obtenerDatosAtletasBDporClub() {
        mDatabase.collection("users")
                .whereEqualTo("club", usuario.getClub())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Usuario usuario = document.toObject(Usuario.class);
                                listaAtletasClubBD.add(usuario);
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinnerCalle1:
                vItemsSeleccionados[0] = i;
                break;
            case R.id.spinnerCalle2:
                vItemsSeleccionados[1] = i;
                break;
            case R.id.spinnerCalle3:
                vItemsSeleccionados[2] = i;
                break;
            case R.id.spinnerCalle4:
                vItemsSeleccionados[3] = i;
                break;
            case R.id.spinnerCalle5:
                vItemsSeleccionados[4] = i;
                break;
            case R.id.spinnerCalle6:
                vItemsSeleccionados[5] = i;
                break;
            case R.id.spinnerCalle7:
                vItemsSeleccionados[6] = i;
                break;


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}