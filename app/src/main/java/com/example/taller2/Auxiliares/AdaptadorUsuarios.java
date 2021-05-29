package com.example.taller2.Auxiliares;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.taller2.Home;
import com.example.taller2.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorUsuarios extends BaseAdapter {

    private DatabaseReference myRef;
    private Context context;
    private ArrayList<UsuarioAux> listaItems;
    private StorageReference mStorageRef;
    private String id;

    public AdaptadorUsuarios(Context context, ArrayList<UsuarioAux> listaItems) {
        this.context = context;
        this.listaItems = listaItems;
    }

    @Override
    public int getCount() {
        return listaItems.size();
    }

    @Override
    public Object getItem(int i) {
        return listaItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        UsuarioAux usuario = (UsuarioAux) getItem(i);
        view = LayoutInflater.from(context).inflate(R.layout.usuario,null);
        ImageView imgFoto = view.findViewById(R.id.imagen);
        TextView nombreUsuario = view.findViewById(R.id.nombreUsuario);
        Button ubicacionUsuario = view.findViewById(R.id.posicionUsuario);

        nombreUsuario.setText(usuario.getUsuario());

        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(usuario.getFoto());
        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imgFoto);
            }
        });

        ubicacionUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Home.class);
                myRef = FirebaseDatabase.getInstance().getReference().child("Usuarios");
                myRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey)
                    {
                        UsuarioAux newPost = dataSnapshot.getValue(UsuarioAux.class);
                        if(newPost.getUsuario().equalsIgnoreCase(usuario.getUsuario()))
                        {
                            System.out.println("SSSSSSSSSSXXXXXXXXXXXXXXx: "+dataSnapshot.getKey());
                            setId(dataSnapshot.getKey());
                            intent.putExtra("codigoUsuario",getId());
                            context.startActivity(intent);
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
            }
        });

        return view;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
