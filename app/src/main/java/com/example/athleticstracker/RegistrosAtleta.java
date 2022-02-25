package com.example.athleticstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RegistrosAtleta extends AppCompatActivity {

    private ArrayList<Registro> lRegistros;
    private ListView listViewRegistros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_registros_atleta);
        this.listViewRegistros = (ListView) findViewById(R.id.listViewRegistros);
        cargarDatos();
        BaseAdapter adapter = crearAdapter();
        this.listViewRegistros.setAdapter(adapter);
    }

    private void cargarDatos(){
        this.lRegistros = (ArrayList<Registro>) getIntent().getExtras().getSerializable("registros");
        if(this.lRegistros.size() == 0){
            Toast.makeText(getApplicationContext(), "Aún no tienes registros personales", Toast.LENGTH_LONG).show();
        }
    }

    private BaseAdapter crearAdapter(){
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return lRegistros.size();
            }

            @Override
            public Object getItem(int i) {
                return lRegistros.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                //Inflamos la vista
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.item_lista_registro, viewGroup, false);

                //Capturamos los elementos del layout
                TextView tiempoRegistro = view.findViewById(R.id.txtTiempoRegistro);
                TextView localidadRegistro = view.findViewById(R.id.textLocalidadRegistro);
                TextView fechaRegistro = view.findViewById(R.id.txtFechaRegistro);
                TextView pruebaRegistro = view.findViewById(R.id.txtPruebaRegistro);

                //Ponemos los valores en los elementos
                tiempoRegistro.setText(tiempoToString(lRegistros.get(i).getTiempo()));
                localidadRegistro.setText(lRegistros.get(i).getLocalidad());
                fechaRegistro.setText(fechaToString(lRegistros.get(i).getFecha()));
                pruebaRegistro.setText(lRegistros.get(i).getPrueba());

                return view;
            }
        };
        return adapter;
    }

    private String tiempoToString(long tiempo){
        DecimalFormat fS = new DecimalFormat("00");
        long decimas = (tiempo%1000)/100;
        long segundos = tiempo/1000;
        long minutos = (segundos/60);
        segundos = segundos%60;
        return String.valueOf(minutos)+":"+fS.format(segundos)+":"+String.valueOf(decimas);
    }

    private String fechaToString(Date date){
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
        return dateFormat.format(date);
    }
}