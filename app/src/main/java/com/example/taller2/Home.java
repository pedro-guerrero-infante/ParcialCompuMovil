package com.example.taller2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
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

    private Marker miMarca;
    private Marker otraMarca;
    private String idOtroUsuario;

    private double latitud1;
    private double latitud2;
    private double longitud1;
    private double longitud2;

    private boolean imprimirNormal = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (imprimirNormal) {
            setImprimirNormal(false);
            imprimirNormal();
        }
    }

    private void sacarUbicacion()
    {
        int permissionLocation1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionLocation1 == PackageManager.PERMISSION_DENIED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser usuario = mAuth.getCurrentUser();
                String id = usuario.getUid();
                myRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(id);
                int n = 0;
                while(n <= 10000)
                {
                    if (n == 10000) {
                        setLongitud1(Double.parseDouble("" + location.getLongitude()));
                        setLatitud1(Double.parseDouble("" + location.getLatitude()));
                        myRef.child("longitud").setValue(""+getLongitud1());
                        myRef.child("latitud").setValue(""+getLatitud1());

                        LatLng usuarioLL = new LatLng(getLatitud1(), getLongitud1());
                        if (getMiMarca() != null) {
                            getMiMarca().remove();
                        }
                        setMiMarca(getmMap().addMarker(new MarkerOptions().position(usuarioLL).title("Mi ubicación")));
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(18));
                    }
                    n++;
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider){}
        };

        permissionLocation1 = ContextCompat.checkSelfPermission(Home.this, Manifest.permission.ACCESS_FINE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
    }

    private void sacarUbicacionDeOtroUsuario() {

        myRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(getIdOtroUsuario());
        setLongitud2(Double.parseDouble(myRef.child("longitud").toString()));
        setLatitud2(Double.parseDouble(myRef.child("latitud").toString()));

        LatLng usuarioSS = new LatLng(getLatitud2(), getLongitud2());
        if (getOtraMarca() != null) {
            getOtraMarca().remove();
        }
        setOtraMarca(getmMap().addMarker(new MarkerOptions().position(usuarioSS).title("Ubicación "+myRef.child("usuario").toString())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        getmMap().moveCamera(CameraUpdateFactory.newLatLng(usuarioSS));

    }

    public void imprimirNormal() {
        int n = 0;
        while(n < 5)
        {
            myRef = FirebaseDatabase.getInstance().getReference().child("locations").child(n + "");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
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

                    if (getOtraMarca() != null) {
                        getOtraMarca().remove();
                    }

                    setOtraMarca(getmMap().addMarker(new MarkerOptions().position(ubi).title(dataSnapshot.child("usuario")
                            .getValue().toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))));


                    getmMap().moveCamera(CameraUpdateFactory.newLatLng(ubi));

                    Toast.makeText(getBaseContext(), "La distancia es: "+ distancia(   getLatitud1(),
                            getLongitud1(),
                            getLatitud2(),
                            getLongitud2())+ "Km", Toast.LENGTH_SHORT).show();
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
        sacarUbicacion();

        String idUsuarioExtra = this.getIntent().getStringExtra("codigoUsuario");
        if (idUsuarioExtra != null) {
            setIdOtroUsuario(idUsuarioExtra);
            colocarMarcadorUsuario(getIdOtroUsuario());
        }
    }

    public double distancia(double lat1, double long1, double lat2, double long2) {

        double latDistance = Math.toRadians(lat1 - lat2);
        double lngDistance = Math.toRadians(long1 - long2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = 6371 * c;
        return Math.round(result*100.0)/100.0;
    }

    public GoogleMap getmMap() {
        return mMap;
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    public boolean isImprimirNormal() {
        return imprimirNormal;
    }

    public void setImprimirNormal(boolean imprimirNormal) {
        this.imprimirNormal = imprimirNormal;
    }

    public double getLatitud1() {
        return latitud1;
    }

    public void setLatitud1(double latitud1) {
        this.latitud1 = latitud1;
    }

    public double getLatitud2() {
        return latitud2;
    }

    public void setLatitud2(double latitud2) {
        this.latitud2 = latitud2;
    }

    public double getLongitud1() {
        return longitud1;
    }

    public void setLongitud1(double longitud1) {
        this.longitud1 = longitud1;
    }

    public double getLongitud2() {
        return longitud2;
    }

    public void setLongitud2(double londitud2) {
        this.longitud2 = londitud2;
    }

    public String getIdOtroUsuario() {
        return idOtroUsuario;
    }

    public void setIdOtroUsuario(String idOtroUsuario) {
        this.idOtroUsuario = idOtroUsuario;
    }

    public Marker getMiMarca() {
        return miMarca;
    }

    public void setMiMarca(Marker miMarca) {
        this.miMarca = miMarca;
    }

    public Marker getOtraMarca() {
        return otraMarca;
    }

    public void setOtraMarca(Marker otraMarca) {
        this.otraMarca = otraMarca;
    }
}