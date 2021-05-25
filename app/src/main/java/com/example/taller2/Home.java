package com.example.taller2;

import androidx.annotation.NonNull;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity implements OnMapReadyCallback {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String PATH_LOCATION = "locations";
    private GoogleMap mMap;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemClicked = item.getItemId();
        if(itemClicked == R.id.menuLogOut){
            mAuth.signOut();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if (itemClicked == R.id.menuSettings){
//Abrir actividad para configuración etc
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final double[] lat = {0};
        final double[] lng = {0};
        System. out. println("Voy aqui");
        database = FirebaseDatabase.getInstance();
        FirebaseUser usuario = mAuth.getCurrentUser();
        String id = usuario.getUid();
        System. out. println("Usuario" + id);
        myRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(id);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    lat[0] = Double.parseDouble(dataSnapshot.child("latitud").getValue().toString());
                    lng[0] = Double.parseDouble(dataSnapshot.child("longitud").getValue().toString());
                    System. out. println("Ahora aqui");
                    System. out. println("Latitud-------" + dataSnapshot.child("latitud").getValue().toString());
                    System. out. println("Longitud-------" + dataSnapshot.child("longitud").getValue().toString());

                    LatLng ubi = new LatLng(Double.parseDouble(dataSnapshot.child("latitud").getValue().toString()), Double.parseDouble(dataSnapshot.child("longitud").getValue().toString()));
                    mMap.addMarker(new MarkerOptions().position(ubi).title("Marker en Sidney"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(ubi));

                    mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                    mMap.getUiSettings().setZoomGesturesEnabled(true);
                    mMap.getUiSettings().setZoomControlsEnabled(true);

                    Marker mfBogota = mMap.addMarker(new MarkerOptions().position(ubi).
                            title("Tu ubicacion")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                }
                System. out. println("Y aqui");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        System. out. println("Latitud" + lat);
        System. out. println("Longitud" + lng);

    }

    public void ubicaciones(){
        final double[] lat = {0};
        final double[] lng = {0};

        myRef = FirebaseDatabase.getInstance().getReference().child("locations");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    lat[0] = Double.parseDouble(dataSnapshot.child("latitude").getValue().toString());
                    lng[0] = Double.parseDouble(dataSnapshot.child("longitude").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}