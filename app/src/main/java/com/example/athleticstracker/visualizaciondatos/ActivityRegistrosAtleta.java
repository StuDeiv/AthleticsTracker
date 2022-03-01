package com.example.athleticstracker.visualizaciondatos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.athleticstracker.R;
import com.example.athleticstracker.entidades.Registro;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Clase que permite mostrar en nuestra aplicación los datos albergados en nuestra base de datos
 * y relativos al atleta que tiene la sesión activa.
 */
public class ActivityRegistrosAtleta extends AppCompatActivity {

    private ArrayList<Registro> lRegistros;
    private ListView listViewRegistros;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros_atleta);
        bundle = getIntent().getExtras();
        this.listViewRegistros = (ListView) findViewById(R.id.listViewRegistros);
        cargarDatos();
        BaseAdapter adapter = crearAdapter();
        this.listViewRegistros.setAdapter(adapter);
    }

    /**
     * Cargamos los datos en el layout correspondiente a esta clase
     */
    private void cargarDatos() {
        this.lRegistros = (ArrayList<Registro>) bundle.getSerializable(getResources().getString(R.string.registros));
        if (this.lRegistros.size() == 0) {
            Toast.makeText(getApplicationContext(), R.string.mensaje_falta_registro_personales, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Método que permite generar el adapter con su layout personalizado y mostrar los datos
     *
     * @return BaseAdapter, devuelve el adaptador ya elaborado
     */
    private BaseAdapter crearAdapter() {
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
                tiempoRegistro.setText(Utilidades.tiempoToString(lRegistros.get(i).getTiempo()));
                localidadRegistro.setText(lRegistros.get(i).getLocalidad());
                fechaRegistro.setText(Utilidades.fechaToString(lRegistros.get(i).getFecha()));
                pruebaRegistro.setText(lRegistros.get(i).getPrueba());

                return view;
            }
        };
        return adapter;
    }
}