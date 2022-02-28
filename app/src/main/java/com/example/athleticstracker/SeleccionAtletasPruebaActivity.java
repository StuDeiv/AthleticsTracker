package com.example.athleticstracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SeleccionAtletasPruebaActivity extends AppCompatActivity {

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
    private int itemSeleccionadoSpinner1;
    private int itemSeleccionadoSpinner2;
    private int itemSeleccionadoSpinner3;
    private int itemSeleccionadoSpinner4;
    private int itemSeleccionadoSpinner5;
    private int itemSeleccionadoSpinner6;
    private int itemSeleccionadoSpinner7;
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
            }
        });

    }

    private void comprobarEstadoCalles() {

        //TODO: CORREGIR IMPORTACIÓN DATOS ACORDE AL ITEM SELECCIONADO

        System.out.println(itemSeleccionadoSpinner1);
        System.out.println(itemSeleccionadoSpinner2);
        System.out.println(itemSeleccionadoSpinner3);
        System.out.println(itemSeleccionadoSpinner4);
        System.out.println(itemSeleccionadoSpinner5);
        System.out.println(itemSeleccionadoSpinner6);
        System.out.println(itemSeleccionadoSpinner7);

    }

    private void inicializarListenerSpinners() {

        spinnerCalle1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemSeleccionadoSpinner1 = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerCalle2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemSeleccionadoSpinner2 = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerCalle3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemSeleccionadoSpinner3 = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerCalle4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemSeleccionadoSpinner4 = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCalle5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemSeleccionadoSpinner5 = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCalle6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemSeleccionadoSpinner6 = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCalle7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemSeleccionadoSpinner7 = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                if (position == itemSeleccionadoSpinner1 || position == itemSeleccionadoSpinner2 || position == itemSeleccionadoSpinner3 || position == itemSeleccionadoSpinner4 || position == itemSeleccionadoSpinner5 || position == itemSeleccionadoSpinner6 || position == itemSeleccionadoSpinner7) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;

                if (position == itemSeleccionadoSpinner1 || position == itemSeleccionadoSpinner2 || position == itemSeleccionadoSpinner3 || position == itemSeleccionadoSpinner4 || position == itemSeleccionadoSpinner5 || position == itemSeleccionadoSpinner6 || position == itemSeleccionadoSpinner7) {
                    textView.setTextColor(Color.GRAY);
                }else{
                    textView.setTextColor(Color.BLACK);
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

}