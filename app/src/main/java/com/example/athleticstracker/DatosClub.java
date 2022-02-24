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
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatosClub extends AppCompatActivity {

    private Club club;
    private TextView txtNombreClub;
    private TextView txtEmailClub;
    private TextView txtLocationClub;
    private ListView listViewCarreras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_club);

        this.txtNombreClub = (TextView) findViewById(R.id.txtNombreClub);
        this.txtEmailClub = (TextView) findViewById(R.id.txtEmailClub);
        this.txtLocationClub = (TextView) findViewById(R.id.txtLocationClub);
        this.listViewCarreras = (ListView) findViewById(R.id.listViewCarreras);

        recuperarDatos();
        cargarDatos();

        BaseAdapter adapter = crearAdapter();
        this.listViewCarreras.setAdapter(adapter);
    }

    private void recuperarDatos(){
        this.club = (Club) getIntent().getExtras().getSerializable("club");
    }

    private void cargarDatos(){
        this.txtNombreClub.setText(this.club.getNombre());
        this.txtEmailClub.setText(this.club.getMail());
        this.txtLocationClub.setText(this.club.getLocalidad());
    }

    private BaseAdapter crearAdapter(){
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return club.lPruebas.size();
            }

            @Override
            public Object getItem(int i) {
                return club.lPruebas.get(i);
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
                TextView txtPruebaCorredores = (TextView) view.findViewById(R.id.txtTipoPrueba);
                TextView txtLocalidadPrueba = (TextView) view.findViewById(R.id.txtLocalidadPrueba);
                TextView txtFechaPrueba = (TextView) view.findViewById(R.id.txtFechaPrueba);

                //Ponemos los valores en los elementos
                txtTipoPrueba.setText(club.getlPruebas().get(i).getTipo());
                txtPruebaCorredores.setText(String.valueOf(club.getlPruebas().get(i).getMapaRegistros().size()));
                txtLocalidadPrueba.setText(club.getlPruebas().get(i).getLocalidad());
                txtFechaPrueba.setText(fechaToString(club.getlPruebas().get(i).getFecha()));

                return view;
            }
        };
        return adapter;
    }

    private String fechaToString(Date date){
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
        return dateFormat.format(date);
    }
}