package com.example.athleticstracker.visualizaciondatos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.athleticstracker.R;
import com.example.athleticstracker.entidades.Registro;
import com.example.athleticstracker.entidades.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Clase que nos permite implementar en nuestra aplicación un Comparador de tiempos entre Atletas, así como
 * su interfaz.
 */
public class ActivityComparador extends AppCompatActivity {

    private Spinner spinnerClub1;
    private Spinner spinnerClub2;
    private Spinner spinnerAtleta1;
    private Spinner spinnerAtleta2;
    private TextView txtEdad1;
    private TextView txtEdad2;
    private ImageButton imgBtnComparar;
    private ListView listViewComparador;

    private Usuario usuario1;
    private Usuario usuario2;
    ArrayList<String[]> lDatos;

    private FirebaseFirestore mDatabase;
    BaseAdapter adapterListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparador);

        mDatabase = FirebaseFirestore.getInstance();
        this.lDatos = new ArrayList<>();

        capturarElementos();
        cargarDatosSpinnerClubes();
        programarClickListenerBoton();


        this.adapterListView = crearAdapter();
        this.listViewComparador.setAdapter(this.adapterListView);
    }

    /**
     * Método que obtiene los datos correspondientes del layout
     */
    private void capturarElementos(){
        this.spinnerClub1 = (Spinner) findViewById(R.id.spinnerClub1);
        this.spinnerClub2 = (Spinner) findViewById(R.id.spinnerClub2);
        this.spinnerAtleta1 = (Spinner) findViewById(R.id.spinnerAtleta1);
        this.spinnerAtleta2 = (Spinner) findViewById(R.id.spinnerAtleta2);
        this.txtEdad1 = (TextView) findViewById(R.id.txtEdad1);
        this.txtEdad2 = (TextView) findViewById(R.id.txtEdad2);
        this.listViewComparador = (ListView) findViewById(R.id.listViewComparador);
        this.imgBtnComparar = (ImageButton) findViewById(R.id.imgBtnComparar);
    }

    /**
     * Método que obtiene los datos de los clubes registrados en la base de datos
     */
    private void cargarDatosSpinnerClubes(){
        ArrayList<String> nombreClubes = new ArrayList<>();
        // Buscamos todos los clubes de nuestra base de datos
        mDatabase.collection(getResources().getString(R.string.clubes))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                nombreClubes.add((String) document.getData().get(getResources().getString(R.string.nombre).toLowerCase())); //Añadimos los nombres a la lista.
                            }
                            // Una vez tenemos la lista con los nombres de los clubs, creamos los adapter de los spinners
                            String[] nombreClubesArray = nombreClubes.toArray(new String[0]);
                            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,nombreClubesArray);
                            adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerClub1.setAdapter(adaptador);
                            spinnerClub2.setAdapter(adaptador);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
        //Una vez hecho esto, vamos a programar lo que sucede cuando seleccionamos un item de los spinners
        programarListenerSpinnerClub(this.spinnerClub1);
        programarListenerSpinnerClub(this.spinnerClub2);
        //También de los spinners de los atletas
        programarListenerSpinnerAtelta(this.spinnerAtleta1);
        programarListenerSpinnerAtelta(this.spinnerAtleta2);
    }

    /**
     * En este método programamos lo que ocurrirá cuando seleccionemos un elemento del spinner.
     * @param spinnerClub Spinner del club a personalizar
     */
    private void programarListenerSpinnerClub(Spinner spinnerClub) {
        spinnerClub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Buscamos los usuarios del club en la base de datos, a través de su UID personalizado
                String uidPersonalizado = spinnerClub.getSelectedItem().toString().replaceAll("\\s+","").toLowerCase();
                ArrayList<Usuario> lUsuariosClub = new ArrayList<>();
                mDatabase.collection(getResources().getString(R.string.users)).whereEqualTo(getResources().getString(R.string.club), uidPersonalizado).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                lUsuariosClub.add(document.toObject(Usuario.class));  //Le añadimos todos los usuarios a una lista
                            }
                            // Por último, evaluamos qué spinner de los dos hemos usado, para cargar los datos en el spinner que le corresponde.
                            // En caso de no haber usuarios en la lista, pondremos el objeto correspondiente a null y cambiamos el texto de la edad.
                            switch(spinnerClub.getId()){
                                case(R.id.spinnerClub1):
                                    cargarDatosSpinnerAtleta(lUsuariosClub, spinnerAtleta1);
                                    if(lUsuariosClub.isEmpty()) {
                                        usuario1 = null;
                                        txtEdad1.setText("");
                                    }
                                break;
                                case(R.id.spinnerClub2):
                                    cargarDatosSpinnerAtleta(lUsuariosClub, spinnerAtleta2);
                                    if(lUsuariosClub.isEmpty()) {
                                        usuario2 = null;
                                        txtEdad2.setText("");
                                    }
                                break;
                            }
                        }
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * En este método cararemos en el spinner correspondiente los datos de la lista de atletas el club seleccionado
     * @param lUsuariosClub
     * @param spinnerAtleta
     */
    public void cargarDatosSpinnerAtleta(ArrayList<Usuario> lUsuariosClub, Spinner spinnerAtleta){
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lUsuariosClub);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAtleta.setAdapter(adapter);
    }

    /**
     * Creamos el onItemSelectedListener para el spinner que llega por parámetros. Dependiendo del lado
     * del comparador que sea, se asignara el Atleta a la variable correspondiente y además calculamos
     * su edad.
     * @param spinnerAtleta
     */
    public void programarListenerSpinnerAtelta(Spinner spinnerAtleta){
        spinnerAtleta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (adapterView.getId()){
                    case(R.id.spinnerAtleta1):
                        usuario1 = (Usuario) spinnerAtleta.getSelectedItem();
                        txtEdad1.setText(calcularEdad(usuario1));
                    break;
                    case(R.id.spinnerAtleta2):
                        usuario2 = (Usuario) spinnerAtleta.getSelectedItem();
                        txtEdad2.setText(calcularEdad(usuario2));
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * Método que nos permite calcular la edad del Usuario respecto a la fecha actual
     * @param usuario Usuario del cual se quiere calcular su edad
     * @return String, con el resultado del cálculo
     */
    public String calcularEdad(Usuario usuario){
        LocalDate hoy = LocalDate.now();
        LocalDate fechaNac = usuario.getFechaNac().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int edad = (int) ChronoUnit.YEARS.between(fechaNac, hoy);
        return String.valueOf(edad)+" años";
    }

    /**
     * Método que nos permite inicializar y programas los listeners de los botones presentes en la interfaz
     */
    public void programarClickListenerBoton(){
        this.imgBtnComparar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usuario1!=null && usuario2!=null){
                    cargarListaDeTiempos();
                    adapterListView.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.mensaje_error_comparador_numero_atletas, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Método que carga y ordena el listado de registro de los atletas que se encuentran en el comparador.
     */
    private void cargarListaDeTiempos(){
        // Ordenamos la lista de registros de los atletas, de menor tiempo a mayor.
        // Así nos aseguramos que el primer registro que encuentra es el mejor tiempo siempre.
        Collections.sort(usuario1.getRegistros());
        Collections.sort(usuario2.getRegistros());

        //Recogemos la lista de pruebas que hay en la aplicacion
        String[] vPruebas = getResources().getStringArray(R.array.pruebas);

        //Creamos la lista donde se almacenaran los datos comparados
        lDatos = new ArrayList<>();
        String[] dato;

        Registro registro;
        int j;
        boolean encontrado1;
        boolean encontrado2;

        // Recorremos completamente el vector de pruebas que hay
        for(int i = 0; i < vPruebas.length; i++){
            j = 0;
            encontrado1 = false;
            encontrado2 = false;
            dato = new String[3];
            dato[1] = vPruebas[i];

            // Buscamos en la listas de registros del atleta1
            while(!encontrado1 && j < usuario1.getRegistros().size()){
                registro = usuario1.getRegistros().get(j);
                if(registro.getPrueba().equals(vPruebas[i])){       //Si encontramos un registro donde la prueba coincida...
                    dato[0] = Utilidades.tiempoToString(registro.getTiempo()); //... lo guardamos
                    encontrado1 = true;
                }
                j++;
            }
            j = 0;
            //Hacemos lo mismo para el segundo atleta
            while(!encontrado2 && j < usuario2.getRegistros().size()){
                registro = usuario2.getRegistros().get(j);
                if(registro.getPrueba().equals(vPruebas[i])){
                    dato[2] = Utilidades.tiempoToString(registro.getTiempo());
                    encontrado2 = true;
                }
                j++;
            }

            //Finalmente, si cualquiera de los dos atletas tiene un registro de esa prueba, lo añadimos a la lista de datos
            if(encontrado1||encontrado2){
                if(dato[0] == null){
                    dato[0] = "S/R";
                }
                if(dato[2] == null){
                    dato[2] = "S/R";
                }
                lDatos.add(dato);
            }
        }
    }

    /**
     * Método que permite generar el adapter con su layout personalizado y mostrar los datos
     * @return BaseAdapter, devuelve el adaptador ya elaborado
     */
    private BaseAdapter crearAdapter(){
        BaseAdapter adapter = new BaseAdapter() {

            @Override
            public int getCount() {
                return lDatos.size();
            }

            @Override
            public String[] getItem(int i) {
                return lDatos.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                //Inflamos la vista
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.item_list_comparador, viewGroup, false);

                //Capturamos los elementos del layout
                TextView txtTiempoComp1= (TextView) view.findViewById(R.id.txtTiempoComp1);
                TextView txtTipoDePruebaComp = (TextView) view.findViewById(R.id.txtTipoDePruebaComp);
                TextView txtTiempoComp2 = (TextView) view.findViewById(R.id.txtTiempoComp2);

                //Ponemos los valores en los elementos
                String[] dato = getItem(i);
                txtTiempoComp1.setText(dato[0]);
                txtTipoDePruebaComp.setText(dato[1]);
                txtTiempoComp2.setText(dato[2]);

                return view;
            }
        };
        return adapter;
    }
}