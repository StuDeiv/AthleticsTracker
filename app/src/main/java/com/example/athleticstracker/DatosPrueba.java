package com.example.athleticstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DatosPrueba extends AppCompatActivity {

    private Prueba prueba;
    private TextView txtLocalidadVistaPrueba;
    private TextView txtTipoDePrueba;
    private TextView txtFechaVistaPrueba;
    private ListView listViewTiempos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_prueba);

        this.txtLocalidadVistaPrueba = (TextView) findViewById(R.id.txtLocalidadVistaPrueba);
        this.txtTipoDePrueba = (TextView) findViewById(R.id.txtTipoDePrueba);
        this.txtFechaVistaPrueba = (TextView) findViewById(R.id.txtFechaVistaPrueba);
        this.listViewTiempos = (ListView) findViewById(R.id.listViewTiempos);

        recuperarDatos();
        cargarDatos();

        BaseAdapter adapter = crearAdapter();
        this.listViewTiempos.setAdapter(adapter);
    }


    private void recuperarDatos(){
        this.prueba = (Prueba) getIntent().getExtras().getSerializable("prueba");
    }

    private void cargarDatos(){
        this.txtLocalidadVistaPrueba.setText(this.prueba.getLocalidad());
        this.txtTipoDePrueba.setText(this.prueba.getTipo());
        this.txtFechaVistaPrueba.setText(fechaToString(this.prueba.getFecha()));
    }

    private BaseAdapter crearAdapter(){
        ArrayList<HashMap.Entry<String, Long>> datos = new ArrayList<>();
        datos.addAll(this.prueba.getMapaTiempos().entrySet());
        Comparator<HashMap.Entry<String, Long>> valueComparator = new Comparator<HashMap.Entry<String,Long>>() {
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
                TextView txtCorredor= (TextView) view.findViewById(R.id.txtCorredor);
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

    private String fechaToString(Date date){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        return dateFormat.format(date);
    }

    private String tiempoToString(long tiempo){
        DecimalFormat fS = new DecimalFormat("00");
        long decimas = (tiempo%1000)/100;
        long segundos = tiempo/1000;
        long minutos = (segundos/60);
        segundos = segundos%60;
        return String.valueOf(minutos)+":"+fS.format(segundos)+":"+String.valueOf(decimas);
    }

}