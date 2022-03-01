package com.example.athleticstracker.visualizaciondatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.athleticstracker.R;
import com.example.athleticstracker.entidades.Club;
import com.example.athleticstracker.entidades.Prueba;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase que permite mostrar en nuestra aplicación los datos albergados en nuestra base de datos
 * y relativos al club del usuario que realiza la consulta en la aplicación
 */
public class DatosClub extends AppCompatActivity {

    private Club club;
    private TextView txtNombreClub;
    private TextView txtEmailClub;
    private TextView txtLocationClub;
    private ListView listViewCarreras;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_club);
        bundle = getIntent().getExtras();
        this.txtNombreClub = (TextView) findViewById(R.id.txtLocalidadVistaPrueba);
        this.txtEmailClub = (TextView) findViewById(R.id.txtTipoDePrueba);
        this.txtLocationClub = (TextView) findViewById(R.id.txtFechaVistaPrueba);
        this.listViewCarreras = (ListView) findViewById(R.id.listViewCarreras);

        recuperarDatos();
        cargarDatos();

        //Generamos el adaptador del ListView de Carreras/Registros
        BaseAdapter adapter = crearAdapter();
        this.listViewCarreras.setAdapter(adapter);
        this.listViewCarreras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Prueba prueba = club.getlPruebas().get(i);
                Intent intent = new Intent(getApplicationContext(), DatosPrueba.class);
                intent.putExtra("prueba", prueba);
                startActivity(intent);
            }
        });
    }

    /**
     * Recuperamos los datos procedentes de la activity origen
     */
    private void recuperarDatos(){
        this.club = (Club) bundle.getSerializable("club");
    }

    /**
     * Cargamos los datos en el layout correspondiente a esta clase
     */
    private void cargarDatos(){
        this.txtNombreClub.setText(this.club.getNombre());
        this.txtEmailClub.setText(this.club.getMail());
        this.txtLocationClub.setText(this.club.getLocalidad());
    }

    /**
     * Método que permite generar el adapter con su layout personalizado y mostrar los datos
     * @return BaseAdapter, devuelve el adaptador ya elaborado
     */
    private BaseAdapter crearAdapter(){
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return club.getlPruebas().size();
            }

            @Override
            public Object getItem(int i) {
                return club.getlPruebas().get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                //Inflamos la vista
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.item_lista_carrera, viewGroup, false);

                //Capturamos los elementos del layout
                TextView txtTipoPrueba = (TextView) view.findViewById(R.id.txtTipoPrueba);
                TextView txtPruebaCorredores = (TextView) view.findViewById(R.id.txtPruebaCorredores);
                TextView txtLocalidadPrueba = (TextView) view.findViewById(R.id.txtLocalidadPrueba);
                TextView txtFechaPrueba = (TextView) view.findViewById(R.id.txtFechaPrueba);

                //Ponemos los valores en los elementos
                txtTipoPrueba.setText(club.getlPruebas().get(i).getTipo());
                txtPruebaCorredores.setText(String.valueOf(club.getlPruebas().get(i).getMapaTiempos().size()));
                txtLocalidadPrueba.setText(club.getlPruebas().get(i).getLocalidad());
                txtFechaPrueba.setText(fechaToString(club.getlPruebas().get(i).getFecha()));

                return view;
            }
        };
        return adapter;
    }

    /**
     * Método que convierte a String y formatea el tiempo que se le pasa por parámetro
     * @param date Tiempo que quiere formatearse y convertirlo a cadena
     * @return String, cadena de texto con el tiempo formateado
     */
    private String fechaToString(Date date){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        return dateFormat.format(date);
    }
}