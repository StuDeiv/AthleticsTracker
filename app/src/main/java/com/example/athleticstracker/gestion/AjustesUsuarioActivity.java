package com.example.athleticstracker.gestion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.athleticstracker.AuthActivity;
import com.example.athleticstracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AjustesUsuarioActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser sesionUsuario;
    private TextView txtViewMailUsuarioAjustes;
    private Button btnActualizarDatosUsuario;
    private Button btnEnviarMensajeVerificacion;
    private Button btnReestablecerContrasenia;
    private Button btnCerrarSesión;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ajustes_usuario);
        iniciarDatos();
        inicializarListeners();
    }


    /**
     * Este método inicia los datos y captura los elementos del layout
      */
    private void iniciarDatos() {
        sesionUsuario = FirebaseAuth.getInstance().getCurrentUser();
        txtViewMailUsuarioAjustes = (TextView) findViewById(R.id.txtViewMailUsuarioAjustes);
        txtViewMailUsuarioAjustes.setText(sesionUsuario.getEmail().toString());
        btnActualizarDatosUsuario = (Button) findViewById(R.id.btnActualizarDatosUsuario);
        btnReestablecerContrasenia = (Button) findViewById(R.id.btnReestablecerContrasenia);
        btnEnviarMensajeVerificacion = (Button) findViewById(R.id.btnEnviarMensajeVerificacion);
        btnCerrarSesión = (Button) findViewById(R.id.btnCerrarSesión);
    }


    /**
     * Método que añade los listeners de los botones
     */
    private void inicializarListeners() {
        btnActualizarDatosUsuario.setOnClickListener(this);
        btnReestablecerContrasenia.setOnClickListener(this);
        btnEnviarMensajeVerificacion.setOnClickListener(this);
        btnCerrarSesión.setOnClickListener(this);
    }

    /**
     * El método on click se llamará cuando se pulsen cualquiera de los botones.
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnActualizarDatosUsuario:
                Intent intent = new Intent(getApplicationContext(), CambioDatosUsuarioActivity.class);
                startActivity(intent);
                break;
            case R.id.btnEnviarMensajeVerificacion:
                enviarMensajeVerificacion();
                break;
            case R.id.btnReestablecerContrasenia:
                alertDialogReestablecerContrasenia();
                break;
            case R.id.btnCerrarSesión:
                alertDialogCerrarSesion();
                break;
        }
    }

    /**
     * Se muestra un diálogo donde una de las opciones es cerrar la sesión
     */
    private void alertDialogCerrarSesion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.titulo_alert_dialog_cerrar_sesion);
        builder.setMessage(R.string.mensaje_titulo_alert_dialog_cerrar_sesion);
        builder.setPositiveButton(R.string.titulo_alert_dialog_cerrar_sesion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();  //Cerramos sesión
                Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    /**
     * Este método envía al usuario un enlace de verificación al correo
     */
    private void enviarMensajeVerificacion() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            alertDialogMensajeVerificacion(); //Mostramos la alerta
                        }
                    }
                });

    }

    /**
     *  Muestra un diálogo de alerta de que se ha enviado el mail de verificación
     */
    private void alertDialogMensajeVerificacion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.titulo_alert_dialog_enviar_mensaje_verificacion);
        builder.setMessage(getResources().getString(R.string.mensaje_alert_dialog_enviar_mensaje_verificacion) + sesionUsuario.getEmail());
        builder.setNegativeButton(R.string.cerrar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    /**
     * En este alert dialog se da la opción de poder cambiar la contraseña.
     */
    private void alertDialogReestablecerContrasenia() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.titulo_alert_dialog_reestablecer_contrasenia);
        builder.setMessage(R.string.mensaje_alert_dialog_reestablecer_contrasenia);

        //Opción que acepta reestablecer contraseña
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(sesionUsuario.getEmail())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), R.string.mensaje_enviado_mail, Toast.LENGTH_SHORT).show();
                                    dialogInterface.cancel();
                                }
                            }
                        });
            }
        });

        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }


}