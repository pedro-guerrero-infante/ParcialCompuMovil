package com.example.taller2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.taller2.Auxiliares.AdaptadorUsuarios;
import com.example.taller2.Auxiliares.UsuarioAux;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Usuarios extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private ArrayList<UsuarioAux> usuarios;
    private ListView lista;
    private AdaptadorUsuarios adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
        subirUsuarios();
    }

    public void subirUsuarios()
    {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser usuario = mAuth.getCurrentUser();
        String id = usuario.getUid();
        setUsuarios(new ArrayList<UsuarioAux>());
        myRef = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey)
            {
                UsuarioAux newPost = dataSnapshot.getValue(UsuarioAux.class);
                if(!id.equalsIgnoreCase(dataSnapshot.getKey()) && newPost.getActivo())
                {
                    getUsuarios().add(newPost);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        lista = (ListView)findViewById(R.id.listaUsuarios);
        adaptador = new AdaptadorUsuarios(this,getUsuarios());
        lista.setAdapter(adaptador);
    }

    public ArrayList<UsuarioAux> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(ArrayList<UsuarioAux> usuarios) {
        this.usuarios = usuarios;
    }
}