package com.example.taller2.Auxiliares;

import android.content.Context;
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

import com.example.taller2.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdaptadorUsuarios extends BaseAdapter {

    private Context context;
    private ArrayList<UsuarioAux> listaItems;
    private StorageReference mStorage;

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
        return view;
    }
}
