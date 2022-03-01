package com.example.athleticstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.rpc.context.AttributeContext;

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Cerrar sesión");
        builder.setMessage("¿Estás seguro de que quieres cerrar sesión?");
        builder.setPositiveButton("CERRAR SESIÓN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO: REVISAR ON DESTROY()
                FirebaseAuth.getInstance().signOut();
                //onDestroy();
                Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void alertDialogEnviarMensajeVerificacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Envíado mensaje de verificación");
        builder.setMessage("Se ha enviado un mensaje de verificación a la siguiente dirección de correo: " + sesionUsuario.getEmail());
        builder.setPositiveButton("Enviar mail", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sesionUsuario.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Mail enviado.Revisa tu bandeja de entrada", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        builder.setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private void alertDialogReestablecerContrasenia() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Reestablecer contraseña");
        builder.setMessage("¿Estás seguro de que deseas reestablecer tu contraseña? Pulsa OK para confirmarlo");

        //Opción que acepta reestablecer contraseña
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(sesionUsuario.getEmail())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Mail enviado.Revisa tu bandeja de entrada", Toast.LENGTH_SHORT).show();
                                    dialogInterface.cancel();
                                }
                            }
                        });
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }


}