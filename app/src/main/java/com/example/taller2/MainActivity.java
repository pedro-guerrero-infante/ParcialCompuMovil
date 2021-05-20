package com.example.taller2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText textoCorreo;
    private EditText textoContraseña;

    //Comentarios jajajajaja

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textoCorreo = findViewById(R.id.etCorreo);
        textoContraseña = findViewById(R.id.etContraseña);

        mAuth = FirebaseAuth.getInstance();
    }

    public void iniciarSesion() {
        signInUser(textoCorreo.getText().toString(), textoContraseña.getText().toString());
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser){
        if(currentUser!=null){
            Intent intent = new Intent(getBaseContext(), Home.class);
            intent.putExtra("user", currentUser.getEmail());
            startActivity(intent);
        } else {
            textoCorreo.setText("");
            textoContraseña.setText("");
        }
    }
//Verifica el formato del email
    private boolean isEmailValid(String email) {
        if (!email.contains("@") ||
                !email.contains(".") ||
                email.length() < 5)
            return false;
        return true;
    }
//Verifica que los campos vengan llenos
    private boolean validateForm() {
        boolean valid = true;
        String email = textoCorreo.getText().toString();
        if (TextUtils.isEmpty(email)) {
            textoCorreo.setError("Required.");
            valid = false;
        } else {
            textoCorreo.setError(null);
        }
        String password = textoContraseña.getText().toString();
        if (TextUtils.isEmpty(password)) {
            textoContraseña.setError("Required.");
            valid = false;
        } else {
            textoContraseña.setError(null);
        }
        return valid;
    }

    private void signInUser(String email, String password) {
        if (validateForm()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
// Sign in success, update UI
                                Log.d("", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
// If sign in fails, display a message to the user.
                                Log.w("", "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }
    }
}