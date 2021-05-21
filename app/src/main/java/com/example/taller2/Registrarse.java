package com.example.taller2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taller2.Auxiliares.UsuarioAux;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registrarse extends AppCompatActivity {

    private EditText usuario;
    private EditText nombre;
    private EditText mail;
    private EditText celular;
    private EditText clave;
    private EditText repetirClave;
    private EditText direccion;

    private TextView btnYaTengoCuenta;
    private Button btnRegistrarse;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        usuario = findViewById(R.id.inputUsername);
        nombre = findViewById(R.id.inputNombre);
        mail = findViewById(R.id.inputEmail);
        celular = findViewById(R.id.inputCelular);
        clave = findViewById(R.id.inputClave);
        repetirClave = findViewById(R.id.inputConfirmarClave);
        direccion = findViewById(R.id.inputDireccion);
        btnYaTengoCuenta = findViewById(R.id.btnYaTengoCuenta);
        btnRegistrarse = findViewById(R.id.btnRegistrar);
    }

    private void mostrarError(EditText editText, String error){
        editText.setError(error);
        editText.requestFocus();
    }

    public void registrarse(View v)
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Usuarios");

        final String usuarios = usuario.getText().toString();
        final String nombres = nombre.getText().toString();
        final String email = mail.getText().toString();
        final String cel = celular.getText().toString();
        final String password = clave.getText().toString();
        final String dir = direccion.getText().toString();
        String valClave = repetirClave.getText().toString();

        int cantidad = password.length();
        boolean bandera = false;

        if (usuarios.isEmpty()){
            mostrarError(usuario,"Debes llenar este campo");
        }
        if (nombres.isEmpty()){
            mostrarError(nombre,"Debes llenar este campo");
        }
        if (email.isEmpty()){
            mostrarError(mail,"Debes llenar este campo");
        }
        if (cel.isEmpty()){
            mostrarError(celular,"Debes llenar este campo");
        }
        if (password.isEmpty()){
            mostrarError(clave,"Debes llenar este campo");
        }
        if (dir.isEmpty()){
            mostrarError(direccion,"Debes llenar este campo");
        }
        else{
            bandera = true;
        }

        if(bandera == true){
            firebaseAuth = FirebaseAuth.getInstance();

            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        UsuarioAux usuarioAux = new UsuarioAux(usuarios,nombres,email,cel,password,dir);
                        databaseReference.child(usuarios).setValue(usuarioAux);

                        Toast.makeText(Registrarse.this,"Usuario Registrado.....",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Registrarse.this,Home.class);

                        intent.putExtra("usuario",usuarios);
                        intent.putExtra("nombre",nombres);
                        intent.putExtra("direccion",dir);
                        intent.putExtra("email",email);
                        intent.putExtra("clave",password);
                        intent.putExtra("celular",cel);

                        startActivity(intent);

                    }
                    else{
                        task.getResult();
                        Toast.makeText(Registrarse.this,task.getResult().toString(),Toast.LENGTH_LONG).show();
//                                Log.i(task.getResult().toString());

                    }
                }
            });
        }
    }
}