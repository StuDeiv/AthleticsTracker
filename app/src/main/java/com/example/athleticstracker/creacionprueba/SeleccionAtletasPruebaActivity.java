package com.example.athleticstracker.creacionprueba;

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
import android.widget.Toast;

import com.example.athleticstracker.R;
import com.example.athleticstracker.entidades.Club;
import com.example.athleticstracker.entidades.Prueba;
import com.example.athleticstracker.entidades.Usuario;
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
    private Usuario[] vCorredores;
    private FirebaseFirestore mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_seleccion_atletas_prueba);
        iniciarDatos();
        anadirListenersBotones();
        inicializarListenerSpinners();

    }

    /**
     * Método que inicia los datos y captura los componentes del layout.
     */
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
        vCorredores = new Usuario[7];
        bundle = getIntent().getExtras();
        mDatabase = FirebaseFirestore.getInstance();
        usuario = (Usuario) bundle.getSerializable(getResources().getString(R.string.usuario));
        prueba = (Prueba) bundle.getSerializable(getResources().getString(R.string.prueba));
        obtenerDatosAtletasBDporClub();
    }

    /**
     * Este método añade los listeners a los botones de CrearConvocatoria y Comenzar prueba
     */
    private void anadirListenersBotones(){
        this.btnImportarCorredores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inicializarSpinners();
            }
        });

        this.btnComenzarPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asignarCorredoresCalles();
                if (comprobarEstadoCalles()){
                    Intent intent = new Intent(getApplicationContext(), ActivityCrono.class);
                    intent.putExtra(getResources().getString(R.string.usuario), usuario);
                    intent.putExtra(getResources().getString(R.string.prueba), prueba);
                    intent.putExtra(getResources().getString(R.string.vCorredores), vCorredores);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),R.string.mensaje_error_calles_vacias_seleccion_atletas,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Iniciamos el ItemSelectedItem de cada spinner
     */
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

    /**
     * Este método comprueba que haya al menos un atleta en la prueba
     * @return true si hay, false si todas las calles están vacías.
     */
    private boolean comprobarEstadoCalles(){
        for (int i = 0; i < vCorredores.length; i++) {
            if (vCorredores[i] != null){
                return true;
            }
        }
        return false;
    }

    /**
     * Este método comprueba los itemes que hay seleccionados en cada spinner y añade el atleta correspondiente
     * al vector de Corredores que pasaremos al siguiente intent.
     */
    private void asignarCorredoresCalles() {
        if (itemSeleccionadoSpinner1 == 0){
            vCorredores[0] = null;
        }else{
            vCorredores[0] = listaAtletasClubBD.get(itemSeleccionadoSpinner1-1);
        }
        if (itemSeleccionadoSpinner2 == 0){
            vCorredores[1] = null;
        }else{
            vCorredores[1] = listaAtletasClubBD.get(itemSeleccionadoSpinner2-1);
        }
        if (itemSeleccionadoSpinner3 == 0){
            vCorredores[2] = null;
        }else{
            vCorredores[2] = listaAtletasClubBD.get(itemSeleccionadoSpinner3-1);
        }
        if (itemSeleccionadoSpinner4 == 0){
            vCorredores[3] = null;
        }else{
            vCorredores[3] = listaAtletasClubBD.get(itemSeleccionadoSpinner4-1);
        }
        if (itemSeleccionadoSpinner5 == 0){
            vCorredores[4] = null;
        }else{
            vCorredores[4] = listaAtletasClubBD.get(itemSeleccionadoSpinner5-1);
        }
        if (itemSeleccionadoSpinner6 == 0){
            vCorredores[5] = null;
        }else{
            vCorredores[5] = listaAtletasClubBD.get(itemSeleccionadoSpinner6-1);
        }
        if (itemSeleccionadoSpinner7 == 0){
            vCorredores[6] = null;
        }else{
            vCorredores[6] = listaAtletasClubBD.get(itemSeleccionadoSpinner7-1);
        }
    }

    /**
     * Método que inicializa los spinners
     */
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

    /**
     * Este método consulta en la base de datos los usuarios que pertenecen al club y los guarda en
     * una lista.
     */
    private void obtenerDatosAtletasBDporClub() {
        mDatabase.collection(getResources().getString(R.string.users))
                .whereEqualTo(getResources().getString(R.string.club), usuario.getClub())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Usuario usuario = document.toObject(Usuario.class);
                                listaAtletasClubBD.add(usuario);
                            }
                        }
                    }
                });
    }

}