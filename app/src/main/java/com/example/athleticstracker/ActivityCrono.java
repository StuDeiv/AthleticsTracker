package com.example.athleticstracker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ActivityCrono extends AppCompatActivity implements View.OnClickListener{

    private Button btnIniciar;
    private Button btnReiniciar;
    private Button btnCalle1;
    private Button btnCalle2;
    private Button btnCalle3;
    private Button btnCalle4;
    private Button btnCalle5;
    private Button btnCalle6;
    private Button btnCalle7;
    private TextView txtPrueba;
    private TextView txtTiempoGeneral;
    private TextView txtTiempo1;
    private TextView txtTiempo2;
    private TextView txtTiempo3;
    private TextView txtTiempo4;
    private TextView txtTiempo5;
    private TextView txtTiempo6;
    private TextView txtTiempo7;
    private ScheduledFuture future;
    private ScheduledThreadPoolExecutor stpe;
    private static final DecimalFormat fS = new DecimalFormat("00");
    long tiempoInicial, tiempoActual, tiempo, decimas, segundos, minutos;
    private int meta; // Contador que se usará para contar las calles que ya han acabado la carrera.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_crono);

        /* Iniciamos los textos, los botones, y ponemos a 0 los corredores que han llegado a la meta */
        iniciarTextos();
        iniciarBotones();
        this.meta = 0;

        /* Método que se llamará cuando hagamos click en el botón INICIAR */
        this.btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarEstadoBotones();
                iniciarCrono();
            }
        });

        /* Método que se llamará cuando hagamos click en el botón REINICIAR */
        this.btnReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                future.cancel(true);
                stpe.shutdown();
                txtTiempoGeneral.setText(getResources().getText(R.string._0_00_0));
                meta = 0;
                iniciarTextos();
                iniciarBotones();
            }
        });

    }

    /**
     * Método que captura los textos y los inicializa. Ocultaremos los tiempos de las calles.
     */
    private void iniciarTextos(){
        this.txtPrueba = (TextView) findViewById(R.id.txtPrueba);
        this.txtTiempoGeneral = (TextView) findViewById(R.id.txtTiempoGeneral);
        this.txtTiempo1 = (TextView) findViewById(R.id.txtTiempo1);
        this.txtTiempo1.setVisibility(View.INVISIBLE);
        this.txtTiempo2 = (TextView) findViewById(R.id.txtTiempo2);
        this.txtTiempo2.setVisibility(View.INVISIBLE);
        this.txtTiempo3 = (TextView) findViewById(R.id.txtTiempo3);
        this.txtTiempo3.setVisibility(View.INVISIBLE);
        this.txtTiempo4 = (TextView) findViewById(R.id.txtTiempo4);
        this.txtTiempo4.setVisibility(View.INVISIBLE);
        this.txtTiempo5 = (TextView) findViewById(R.id.txtTiempo5);
        this.txtTiempo5.setVisibility(View.INVISIBLE);
        this.txtTiempo6 = (TextView) findViewById(R.id.txtTiempo6);
        this.txtTiempo6.setVisibility(View.INVISIBLE);
        this.txtTiempo7 = (TextView) findViewById(R.id.txtTiempo7);
        this.txtTiempo7.setVisibility(View.INVISIBLE);
    }

    /**
     * Método que captura los botones de la vista y los inicializa. De primeras, solo estará habili-
     * tado el botón de Iniciar el cronómetro.
     */
    private void iniciarBotones(){
        this.btnIniciar = (Button) findViewById(R.id.btnIniciar);
        this.btnIniciar.setEnabled(true);
        this.btnReiniciar = (Button) findViewById(R.id.btnReiniciar);
        this.btnReiniciar.setEnabled(false);
        this.btnCalle1 = (Button) findViewById(R.id.btnCalle1);
        this.btnCalle1.setEnabled(false);
        this.btnCalle1.setOnClickListener(this);
        this.btnCalle2 = (Button) findViewById(R.id.btnCalle2);
        this.btnCalle2.setEnabled(false);
        this.btnCalle2.setOnClickListener(this);
        this.btnCalle3 = (Button) findViewById(R.id.btnCalle3);
        this.btnCalle3.setEnabled(false);
        this.btnCalle3.setOnClickListener(this);
        this.btnCalle4 = (Button) findViewById(R.id.btnCalle4);
        this.btnCalle4.setEnabled(false);
        this.btnCalle4.setOnClickListener(this);
        this.btnCalle5 = (Button) findViewById(R.id.btnCalle5);
        this.btnCalle5.setEnabled(false);
        this.btnCalle5.setOnClickListener(this);
        this.btnCalle6 = (Button) findViewById(R.id.btnCalle6);
        this.btnCalle6.setEnabled(false);
        this.btnCalle6.setOnClickListener(this);
        this.btnCalle7 = (Button) findViewById(R.id.btnCalle7);
        this.btnCalle7.setEnabled(false);
        this.btnCalle7.setOnClickListener(this);
    }

    /**
     * Método que cambia el estado de los botones. Deshabilita el botón de iniciar y habilita los
     * botones de STOP de todas las calles. Se llamará cuando se inicie el crono.
     */
    private void cambiarEstadoBotones(){
        this.btnIniciar.setEnabled(false);
        this.btnCalle1.setEnabled(true);
        this.btnCalle2.setEnabled(true);
        this.btnCalle3.setEnabled(true);
        this.btnCalle4.setEnabled(true);
        this.btnCalle5.setEnabled(true);
        this.btnCalle6.setEnabled(true);
        this.btnCalle7.setEnabled(true);
    }

    /**
     * Método que inicia el cronómetro. La idea es crear una tarea que se ejecute cada 100ms.
     * Esa taréa lo que hará será comparar el tiempo del sistema que capturamos al llamar al método
     * por primera vez, con el tiempo del sistema que capturaremos cada vez que ejecutamos la tarea.
     * Una vez calculado esa diferencia, actualizaremos el texto del cronómetro.
     * Esta forma de hacerlo permite tener una precisión muy alta ya que System.currenTimeMillis()
     * da la hora del sistema con precisión de milisegundos, solo superada por System.nanoTime.
     */
    private void iniciarCrono(){
        this.stpe = new ScheduledThreadPoolExecutor(1);
        tiempoInicial = System.currentTimeMillis();
        this.future = this.stpe.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tiempoActual = System.currentTimeMillis();  //Tiempo actual en ms
                        tiempo = tiempoActual - tiempoInicial;      //Diferencia real en ms.
                        decimas = (tiempo%1000)/100;
                        segundos = tiempo/1000;
                        minutos = segundos/60;
                        segundos = segundos%60;
                        txtTiempoGeneral.setText(String.valueOf(minutos)+":"+fS.format(segundos)+":"+String.valueOf(decimas));
                    }
                });
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
        this.btnReiniciar.setEnabled(true); //Activamos el botón de reiniciar
    }

    /**
     * Método que se llamará cuando pulsemos click sobre uno delos botones de STOP de las calles.
     * Comprueba de qué calle es el botón para grabar el tiempo en el TextViewCorrespondiente.
     * La idea es coger el valor del tiempo que haya en ese instante en el TiempoGeneral y copiarlo,
     * hacer visible el text view e inhabilitar el botón.
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnCalle1:
                this.txtTiempo1.setText(this.txtTiempoGeneral.getText()); //Copiamos el texto.
                this.txtTiempo1.setVisibility(View.VISIBLE); //Lo hacemos visible
                this.btnCalle1.setEnabled(false); //Deshabilitamos ese botón
                this.meta++; //Incrementamos el número de corredores que ha llegado a meta
                break;
            case R.id.btnCalle2:
                this.txtTiempo2.setText(this.txtTiempoGeneral.getText());
                this.txtTiempo2.setVisibility(View.VISIBLE);
                this.btnCalle2.setEnabled(false);
                this.meta++;
                break;
            case R.id.btnCalle3:
                this.txtTiempo3.setText(this.txtTiempoGeneral.getText());
                this.txtTiempo3.setVisibility(View.VISIBLE);
                this.btnCalle3.setEnabled(false);
                this.meta++;
                break;
            case R.id.btnCalle4:
                this.txtTiempo4.setText(this.txtTiempoGeneral.getText());
                this.txtTiempo4.setVisibility(View.VISIBLE);
                this.btnCalle4.setEnabled(false);
                this.meta++;
                break;
            case R.id.btnCalle5:
                this.txtTiempo5.setText(this.txtTiempoGeneral.getText());
                this.txtTiempo5.setVisibility(View.VISIBLE);
                this.btnCalle5.setEnabled(false);
                this.meta++;
                break;
            case R.id.btnCalle6:
                this.txtTiempo6.setText(this.txtTiempoGeneral.getText());
                this.txtTiempo6.setVisibility(View.VISIBLE);
                this.btnCalle6.setEnabled(false);
                this.meta++;
                break;
            case R.id.btnCalle7:
                this.txtTiempo7.setText(this.txtTiempoGeneral.getText());
                this.txtTiempo7.setVisibility(View.VISIBLE);
                this.btnCalle7.setEnabled(false);
                this.meta++;
                break;
        }
        /* Cuando todos los corredores han llegado a la meta, cancelamos la tarea que actualiza el reloj */
        if(this.meta == 7){
            this.future.cancel(false);
            this.stpe.shutdown();
        }
    }

    /**
     * Cuando la Activity finalice su ciclo de vida, queremos que se dejen de tener las tareas en
     * segundo plano.
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(this.future != null){
            if(!this.future.isCancelled()) {
                this.future.cancel(false);
            }
        }
        if(this.stpe != null){
            this.stpe.shutdown();
        }
    }
}