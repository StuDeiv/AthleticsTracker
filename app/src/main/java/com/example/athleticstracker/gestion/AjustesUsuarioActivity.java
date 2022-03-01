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

    private void inicializarListeners() {
        btnActualizarDatosUsuario.setOnClickListener(this);
        btnReestablecerContrasenia.setOnClickListener(this);
        btnEnviarMensajeVerificacion.setOnClickListener(this);
        btnCerrarSesión.setOnClickListener(this);
    }

    private void iniciarDatos() {
        sesionUsuario = FirebaseAuth.getInstance().getCurrentUser();
        txtViewMailUsuarioAjustes = (TextView) findViewById(R.id.txtViewMailUsuarioAjustes);
        txtViewMailUsuarioAjustes.setText(sesionUsuario.getEmail().toString());
        btnActualizarDatosUsuario = (Button) findViewById(R.id.btnActualizarDatosUsuario);
        btnReestablecerContrasenia = (Button) findViewById(R.id.btnReestablecerContrasenia);
        btnEnviarMensajeVerificacion = (Button) findViewById(R.id.btnEnviarMensajeVerificacion);
        btnCerrarSesión = (Button) findViewById(R.id.btnCerrarSesión);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnActualizarDatosUsuario:
                Intent intent = new Intent(getApplicationContext(), CambioDatosUsuarioActivity.class);
                startActivity(intent);
                break;
            case R.id.btnEnviarMensajeVerificacion:
                alertDialogEnviarMensajeVerificacion();
                break;
            case R.id.btnReestablecerContrasenia:
                alertDialogReestablecerContrasenia();
                break;
            case R.id.btnCerrarSesión:
                alertDialogCerrarSesion();
                break;
        }
    }

    private void alertDialogCerrarSesion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.titulo_alert_dialog_cerrar_sesion);
        builder.setMessage(R.string.mensaje_titulo_alert_dialog_cerrar_sesion);
        builder.setPositiveButton(R.string.titulo_alert_dialog_cerrar_sesion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
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

    private void alertDialogEnviarMensajeVerificacion() {
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