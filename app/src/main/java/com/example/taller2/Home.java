package com.example.taller2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.taller2.Auxiliares.UsuarioAux;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements OnMapReadyCallback {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private GoogleMap mMap;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String idUsuarioExtra = this.getIntent().getStringExtra("codigoUsuario");
        if(idUsuarioExtra != null)
        {
            colocarMarcadorUsuario(idUsuarioExtra);
        }
    }

    public void colocarMarcadorUsuario(String uidUsuario)
    {
        database = FirebaseDatabase.getInstance();

        myRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(uidUsuario);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    LatLng ubi = new LatLng(Double.parseDouble(dataSnapshot.child("latitud").getValue().toString()),
                            Double.parseDouble(dataSnapshot.child("longitud").getValue().toString()));

                    getmMap().addMarker(new MarkerOptions().position(ubi).title(dataSnapshot.child("usuario")
                            .getValue().toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                    getmMap().moveCamera(CameraUpdateFactory.newLatLng(ubi));

                    getmMap().moveCamera(CameraUpdateFactory.zoomTo(15));
                    getmMap().getUiSettings().setZoomGesturesEnabled(true);
                    getmMap().getUiSettings().setZoomControlsEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemClicked = item.getItemId();
        if(itemClicked == R.id.menuLogOut) {
            mAuth.signOut();
            Intent intent = new Intent(Home.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }else if (itemClicked == R.id.menuSettings){
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser usuario = mAuth.getCurrentUser();
            String id = usuario.getUid();
            myRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(id);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists())
                    {
                        boolean estado = (boolean) dataSnapshot.child("activo").getValue();
                        if(estado == false) {
                            myRef.child("activo").setValue(true);
                            Toast.makeText(getBaseContext(), "Disponibilidad activada", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }else if(itemClicked == R.id.menuUsuarios) {
            Intent intent = new Intent(Home.this, Usuarios.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setmMap(googleMap);
        database = FirebaseDatabase.getInstance();
        FirebaseUser usuario = mAuth.getCurrentUser();
        String id = usuario.getUid();

        myRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(id);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    LatLng ubi = new LatLng(Double.parseDouble(dataSnapshot.child("latitud").getValue().toString()),
                            Double.parseDouble(dataSnapshot.child("longitud").getValue().toString()));
                    getmMap().addMarker(new MarkerOptions().position(ubi).title("Tu ubicación"));
                    getmMap().moveCamera(CameraUpdateFactory.newLatLng(ubi));

                    getmMap().moveCamera(CameraUpdateFactory.zoomTo(15));
                    getmMap().getUiSettings().setZoomGesturesEnabled(true);
                    getmMap().getUiSettings().setZoomControlsEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        int n = 0;
        while(n < 5) {

            myRef = FirebaseDatabase.getInstance().getReference().child("locations").child(n+"");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        System.out.println(dataSnapshot.child("latitude").getValue().toString());
                        System.out.println(dataSnapshot.child("longitude").getValue().toString());
                        System.out.println(dataSnapshot.child("name").getValue().toString());


                        LatLng ubi = new LatLng(Double.parseDouble(dataSnapshot.child("latitude").getValue().toString()),
                                                Double.parseDouble(dataSnapshot.child("longitude").getValue().toString()));

                        getmMap().addMarker(new MarkerOptions().position(ubi).title(dataSnapshot.child("name")
                                .getValue().toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        getmMap().moveCamera(CameraUpdateFactory.newLatLng(ubi));
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            n++;
        }
    }

    public GoogleMap getmMap() {
        return mMap;
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }
}