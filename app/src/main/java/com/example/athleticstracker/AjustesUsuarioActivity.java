package com.example.athleticstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AjustesUsuarioActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser sesionUsuario;
    private TextView txtViewMailUsuarioAjustes;
    private Button btnActualizarMail;
    private Button btnEnviarMensajeVerificacion;
    private Button btnReestablecerContraseña;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ajustes_usuario);
        iniciarDatos();

    }

    private void iniciarDatos() {
        sesionUsuario = FirebaseAuth.getInstance().getCurrentUser();
        txtViewMailUsuarioAjustes = (TextView) findViewById(R.id.txtViewMailUsuarioAjustes);
        txtViewMailUsuarioAjustes.setText(sesionUsuario.getEmail().toString());
        btnActualizarMail = (Button) findViewById(R.id.btnActualizarMail);
        btnReestablecerContraseña = (Button) findViewById(R.id.btnReestablecerContraseña);
        btnEnviarMensajeVerificacion = (Button) findViewById(R.id.btnEnviarMensajeVerificacion);
        btnActualizarMail.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnActualizarMail:
                break;
            case R.id.btnEnviarMensajeVerificacion:
                break;
            case R.id.btnReestablecerContraseña:
                break;
        }
    }



}