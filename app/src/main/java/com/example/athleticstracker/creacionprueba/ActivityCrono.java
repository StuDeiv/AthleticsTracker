package com.example.athleticstracker.creacionprueba;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.athleticstracker.MenuUsuario;
import com.example.athleticstracker.R;
import com.example.athleticstracker.entidades.Prueba;
import com.example.athleticstracker.entidades.Registro;
import com.example.athleticstracker.entidades.Usuario;
import com.example.athleticstracker.visualizaciondatos.DatosPrueba;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ActivityCrono extends AppCompatActivity implements View.OnClickListener{

    private Bundle bundle;
    private Button btnIniciar;
    private Button btnReiniciar;
    private TextView txtPrueba;
    private TextView txtTiempoGeneral;
    private TextView[] vTxtTiempos;
    private Button[] vBtnCalles;
    private ScheduledFuture future;
    private ScheduledThreadPoolExecutor stpe;
    private static final DecimalFormat fS = new DecimalFormat("00");
    long tiempoInicial, tiempoActual, tiempo, decimas, segundos, minutos;
    private int meta; // Contador que se usará para contar las calles que ya han acabado la carrera.
    private int corredores; // Número de corredores que hay en la prueba
    private Usuario[] vUsuarios;
    private Usuario entrenador;
    private Prueba prueba;
    private String club;
    private HashMap<String, Registro> mapaRegistros;
    private FirebaseFirestore mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_crono);

        /* Iniciamos los datos, textos y botones */
        this.mDatabase = FirebaseFirestore.getInstance();
        this.mapaRegistros = new HashMap<>();
        this.meta = 0;
        iniciarDatos();
        capturarTextos();
        capturarBotones();
        anadirListenersIniciarReiniciar();

    }

    /**
     * Recogemos los datos del intent
     */
    private void iniciarDatos(){
        bundle = getIntent().getExtras();
        this.vUsuarios = (Usuario[]) bundle.getSerializable(getResources().getString(R.string.vCorredores));
        this.entrenador = (Usuario) bundle.getSerializable(getResources().getString(R.string.usuario));
        this.prueba = (Prueba) bundle.getSerializable(getResources().getString(R.string.prueba).toLowerCase());
        this.club = entrenador.getClub();

        /* Para calcular los corredores que hay en la Prueba, se mira el vector */
        for(int i = 0; i < vUsuarios.length; i++){
            if(vUsuarios[i] != null){ // Si la posición no está vacía, habrá un corredor en esa calle
                corredores++;
            }
        }
    }

    /**
     * Método que captura los textos de la vista.
     */
    private void capturarTextos(){
        this.txtPrueba = (TextView) findViewById(R.id.txtPrueba);
        this.txtPrueba.setText(this.prueba.getTipo());
        this.txtTiempoGeneral = (TextView) findViewById(R.id.txtTiempoGeneral);
        this.vTxtTiempos = new TextView[7];
        int[] vRecursos = {R.id.txtTiempo1, R.id.txtTiempo2, R.id.txtTiempo3, R.id.txtTiempo4 ,R.id.txtTiempo5 ,R.id.txtTiempo6, R.id.txtTiempo7};
        for(int i = 0; i < vUsuarios.length; i++){
            this.vTxtTiempos[i] = (TextView) findViewById(vRecursos[i]);
            if(vUsuarios[i] != null){
                this.vTxtTiempos[i].setText(this.vUsuarios[i].getNombre()); //Ponemos el nombre del corredor
                this.vTxtTiempos[i].setVisibility(View.VISIBLE); // Hacemos visible el texto
            }else{
                this.vTxtTiempos[i].setVisibility(View.INVISIBLE); //Si no hay corredor en esa calle, se oculta el texto
            }
        }
    }

    /**
     * Método que captura los botones de la vista. (También les añade el clickListener)
     */
    private void capturarBotones(){
        this.btnIniciar = (Button) findViewById(R.id.btnIniciar);
        this.btnIniciar.setEnabled(true);
        this.btnReiniciar = (Button) findViewById(R.id.btnReiniciar);
        this.btnReiniciar.setEnabled(false);
        this.vBtnCalles = new Button[7];
        int[] vRecursos = {R.id.btnCalle1, R.id.btnCalle2, R.id.btnCalle3, R.id.btnCalle4, R.id.btnCalle5, R.id.btnCalle6, R.id.btnCalle7};
        for(int i = 0; i < vUsuarios.length; i++){
            this.vBtnCalles[i] = (Button) findViewById(vRecursos[i]);
            this.vBtnCalles[i].setOnClickListener(this);
            this.vBtnCalles[i].setEnabled(false);
            if(vUsuarios[i] == null){
                this.vBtnCalles[i].setVisibility(View.INVISIBLE); //Si no hay corredor en esa calle, se oculta el botón
            }
        }
    }

    /**
     *  Les añadimos un listener personalizado al botón de Iniciar y al de Reiniciar.
     */
    private void anadirListenersIniciarReiniciar(){
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
                reiniciarTextosYBotones();
            }
        });
    }

    /**
     * Método que cambia el estado de los botones. Deshabilita el botón de iniciar y habilita los
     * botones de STOP de las calles donde hay corredores. Se llamará cuando se inicie el crono.
     */
    private void cambiarEstadoBotones(){
        this.btnIniciar.setEnabled(false);
        this.btnReiniciar.setEnabled(true);
        for(int i = 0; i < this.vBtnCalles.length; i++){
            if(this.vUsuarios[i] != null){
                this.vBtnCalles[i].setEnabled(true);
            }
        }
    }

    /**
     * Método que reinicia el estado inicial de los botones.
     */
    private void reiniciarTextosYBotones(){
        this.btnIniciar.setEnabled(true);
        this.btnReiniciar.setEnabled(false);
        for(int i = 0; i < this.vBtnCalles.length; i++){
            if(this.vUsuarios[i] != null) {
                this.vBtnCalles[i].setEnabled(true); //Se activa el botón
                this.vTxtTiempos[i].setText(vUsuarios[i].getNombre()); //Se vuelve a poner el nombre del corredor, por si se había cambiado
            }
        }
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
                        //No llamo al método Utilidad.tiempoToString ni creo recursos para ":" para que no se vea afectado el rendimiento
                    }
                });
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }


    /**
     * Método que se llamará cuando pulsemos click sobre uno delos botones de STOP de las calles.
     * Captura el tiempo que haya en ese instante la variable global "tiempo". Luego se evalúa desde
     * que calle se ha pulsado el botón y asignamos los tiempos.
     * @param view la vista que representa el botón donde se ha pulsado
     */
    @Override
    public void onClick(View view) {
        long tiempoRegistro = tiempo;
        switch(view.getId()){
            case R.id.btnCalle1:
                asignarTiempos(0, tiempo);
                break;
            case R.id.btnCalle2:
                asignarTiempos(1, tiempo);
                break;
            case R.id.btnCalle3:
                asignarTiempos(2, tiempo);
                break;
            case R.id.btnCalle4:
                asignarTiempos(3, tiempo);
                break;
            case R.id.btnCalle5:
                asignarTiempos(4, tiempo);
                break;
            case R.id.btnCalle6:
                asignarTiempos(5, tiempo);
                break;
            case R.id.btnCalle7:
                asignarTiempos(6, tiempo);
                break;
        }
        /* Cuando todos los corredores han llegado a la meta, le pedimos al usuario qué quiere hacer */
        if(this.meta == this.corredores){
            finalDeLaPrueba();
        }
    }

    /**
     * Método que se llamará cuando se termine la carrera.
     */
    private void finalDeLaPrueba(){
        this.future.cancel(true);
        this.stpe.shutdown();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.carrera_finalizada);
        builder.setMessage(R.string.mensaje_carrera_finalizada);
        /* Un botón para guardar los resultados y mostrarlos */
        builder.setPositiveButton(R.string.guardar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                persistirDatos();
                Intent intent = new Intent(getApplicationContext(), DatosPrueba.class);
                intent.putExtra(getResources().getString(R.string.prueba), prueba);
                startActivity(intent);
                finish();
            }
        });
        /* Otro botón para reiniciar la competición */
        builder.setNeutralButton(R.string.reiniciar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                txtTiempoGeneral.setText(getResources().getText(R.string._0_00_0));
                meta = 0;
                reiniciarTextosYBotones();
            }
        });
        /* Otro botón para cerrar y volver a la pantalla de inicio */
        builder.setNegativeButton(R.string.volver_iniciar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(), MenuUsuario.class);
                intent.putExtra(getResources().getString(R.string.usuario), entrenador);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        builder.show();
    }


    /**
     * Este método registra los tiempos de cada atleta.
     * @param posicion
     * @param tiempo
     */
    private void asignarTiempos(int posicion, long tiempo){
        /* Primero añadimos los datos de ese tiempo a la prueba */
        String nombre = vUsuarios[posicion].getNombre()+getResources().getString(R.string.espacio)+vUsuarios[posicion].getApellidos(); //Cogemos el nombre del atleta
        this.prueba.getMapaTiempos().put(nombre, tiempo); //Añadimos el registro a la prueba

        /* Ahora creamos un objeto Registro individual y lo asociamos al email del atleta dentor de un mapa */
        String email = vUsuarios[posicion].getEmail(); // Cogemos el email del atleta, identificador único
        Registro registro = new Registro(tiempo, this.prueba.getLocalidad(), this.prueba.getTipo(), new Date());
        this.mapaRegistros.put(email, registro);

        /* Cambiamos los datos de los textos correspondientes */
        this.vTxtTiempos[posicion].setText(this.txtTiempoGeneral.getText()); //Copiamos el texto del tiempo.
        this.vTxtTiempos[posicion].setVisibility(View.VISIBLE); //Lo hacemos visible

        this.vBtnCalles[posicion].setEnabled(false); //Deshabilitamos ese botón
        this.meta++; //Incrementamos el número de corredores que ha llegado a meta

    }

    /**
     * Este método persiste los datos en la bbdd
     */
    public void persistirDatos(){
        mDatabase.collection(getResources().getString(R.string.clubes)).document(this.club).update(getResources().getString(R.string.lpruebas), FieldValue.arrayUnion(this.prueba));
        for(HashMap.Entry<String, Registro> entry : this.mapaRegistros.entrySet()){
            mDatabase.collection(getResources().getString(R.string.users)).document(entry.getKey()).update(getResources().getString(R.string.registros), FieldValue.arrayUnion(entry.getValue()));
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