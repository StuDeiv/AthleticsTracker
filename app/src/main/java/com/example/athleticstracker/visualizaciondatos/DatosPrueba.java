package com.example.athleticstracker.visualizaciondatos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.athleticstracker.R;
import com.example.athleticstracker.entidades.Prueba;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

/**
 * Clase que permite mostrar en nuestra aplicación los datos albergados en nuestra base de datos
 * y relativos a la prueba consultada por el usuario.
 */
public class DatosPrueba extends AppCompatActivity {

    private Prueba prueba;
    private TextView txtLocalidadVistaPrueba;
    private TextView txtTipoDePrueba;
    private TextView txtFechaVistaPrueba;
    private ListView listViewTiempos;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_prueba);
        bundle = getIntent().getExtras();
        this.txtLocalidadVistaPrueba = (TextView) findViewById(R.id.txtLocalidadVistaPrueba);
        this.txtTipoDePrueba = (TextView) findViewById(R.id.txtTipoDePrueba);
        this.txtFechaVistaPrueba = (TextView) findViewById(R.id.txtFechaVistaPrueba);
        this.listViewTiempos = (ListView) findViewById(R.id.listViewTiempos);

        recuperarDatos();
        cargarDatos();

        BaseAdapter adapter = crearAdapter();
        this.listViewTiempos.setAdapter(adapter);
    }

    /**
     * Recuperamos los datos procedentes de la activity origen
     */
    private void recuperarDatos() {
        this.prueba = (Prueba) bundle.getSerializable("prueba");
    }

    /**
     * Cargamos los datos en el layout correspondiente a esta clase
     */
    private void cargarDatos() {
        this.txtLocalidadVistaPrueba.setText(this.prueba.getLocalidad());
        this.txtTipoDePrueba.setText(this.prueba.getTipo());
        this.txtFechaVistaPrueba.setText(fechaToString(this.prueba.getFecha()));
    }

    /**
     * Método que permite generar el adapter con su layout personalizado y mostrar los datos
     * @return BaseAdapter, devuelve el adaptador ya elaborado
     */
    private BaseAdapter crearAdapter() {
        ArrayList<HashMap.Entry<String, Long>> datos = new ArrayList<>();
        datos.addAll(this.prueba.getMapaTiempos().entrySet());
        Comparator<HashMap.Entry<String, Long>> valueComparator = new Comparator<HashMap.Entry<String, Long>>() {
            @Override
            public int compare(HashMap.Entry<String, Long> e1, HashMap.Entry<String, Long> e2) {
                Long v1 = e1.getValue();
                Long v2 = e2.getValue();
                return v1.compareTo(v2);
            }
        };
        Collections.sort(datos, valueComparator);

        BaseAdapter adapter = new BaseAdapter() {

            @Override
            public int getCount() {
                return datos.size();
            }

            @Override
            public HashMap.Entry<String, Long> getItem(int i) {
                return datos.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                //Inflamos la vista
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.item_lista_tiempo, viewGroup, false);

                //Capturamos los elementos del layout
                TextView txtCorredor = (TextView) view.findViewById(R.id.txtCorredor);
                TextView txtTiempo = (TextView) view.findViewById(R.id.txtTiempo);

                //Ponemos los valores en los elementos
                HashMap.Entry<String, Long> item = getItem(i);
                txtCorredor.setText(item.getKey());
                txtTiempo.setText(tiempoToString(item.getValue()));

                return view;
            }
        };
        return adapter;
    }

    /**
     * Método que convierte la fecha que pasa por parametro a String y formatea el tiempo que se le pasa por parámetro
     * @param date Tiempo que quiere formatearse y convertirlo a cadena
     * @return String, cadena de texto con el tiempo formateado
     */
    private String fechaToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        return dateFormat.format(date);
    }

    /**
     * Método que convierte el tiempo de la prueba a String y formatea el tiempo que se le pasa por parámetro
     * @param tiempo Tiempo que quiere formatearse y convertirlo a cadena
     * @return String, cadena de texto con el tiempo formateado
     */
    private String tiempoToString(long tiempo) {
        DecimalFormat fS = new DecimalFormat("00");
        long decimas = (tiempo % 1000) / 100;
        long segundos = tiempo / 1000;
        long minutos = (segundos / 60);
        segundos = segundos % 60;
        return String.valueOf(minutos) + ":" + fS.format(segundos) + ":" + String.valueOf(decimas);
    }

}