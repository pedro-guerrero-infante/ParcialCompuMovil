package com.example.taller2;

import androidx.annotation.NonNull;
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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taller2.Auxiliares.UsuarioAux;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;


public class Registrarse extends AppCompatActivity {

    private String usuarioC;
    private String nombreC;
    private String apellidoC;
    private String emailC;
    private String cedulaC;
    private String claveC;
    private String repetirClaveC;
    private String latitudC;
    private String longitudC;

    private EditText usuario;
    private EditText nombre;
    private EditText apellido;
    private EditText email;
    private EditText cedula;
    private EditText clave;
    private EditText repetirClave;

    private TextView btnYaTengoCuenta;
    private Button btnRegistrarse;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;

    private int IMAGE_PICKER_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        sacarUbicacion();
        permiso();
    }

    private void mostrarError(EditText editText, String error){
        editText.setError(error);
        editText.requestFocus();
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
                setLatitudC(""+location.getLatitude());
                setLongitudC(""+location.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider){}
        };

        permissionLocation1 = ContextCompat.checkSelfPermission(Registrarse.this, Manifest.permission.ACCESS_FINE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
    }

    public void registrarse(View v)
    {
        usuario = findViewById(R.id.usuario);
        nombre = findViewById(R.id.nombre);
        apellido = findViewById(R.id.apellido);
        email = findViewById(R.id.email);
        cedula = findViewById(R.id.cedula);
        clave = findViewById(R.id.clave);
        repetirClave = findViewById(R.id.confirmarClave);

        btnYaTengoCuenta = findViewById(R.id.btnYaTengoCuenta);
        btnRegistrarse = findViewById(R.id.btnRegistrar);

        setUsuarioC(usuario.getText().toString());
        setNombreC(nombre.getText().toString());
        setApellidoC(apellido.getText().toString());
        setEmailC(email.getText().toString());
        setCedulaC(cedula.getText().toString());
        setClaveC(clave.getText().toString());
        setRepetirClaveC(repetirClave.getText().toString());

        boolean bandera = true;

        if (getUsuarioC().isEmpty())
        {
            bandera = false;
            mostrarError(usuario,"Debes llenar este campo");
        }
        if (getNombreC().isEmpty())
        {
            bandera = false;
            mostrarError(nombre,"Debes llenar este campo");
        }
        if (getApellidoC().isEmpty())
        {
            bandera = false;
            mostrarError(apellido,"Debes llenar este campo");
        }
        if (getEmailC().isEmpty())
        {
            bandera = false;
            mostrarError(email,"Debes llenar este campo");
        }
        if (getCedulaC().isEmpty())
        {
            bandera = false;
            mostrarError(cedula,"Debes llenar este campo");
        }
        if (getClaveC().isEmpty())
        {
            bandera = false;
            mostrarError(clave,"Debes llenar este campo");
        }
        if (getRepetirClaveC().isEmpty())
        {
            bandera = false;
            mostrarError(repetirClave,"Debes llenar este campo");
        }
        else
        {
            if(!getClaveC().equals(getRepetirClaveC()))
            {
                bandera = false;
                mostrarError(repetirClave,"No son iguales las contraseñas");
            }
        }

        if(bandera == true)
        {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Usuarios");

            firebaseAuth.createUserWithEmailAndPassword(emailC,claveC).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        UsuarioAux usuarioAux = new UsuarioAux(getUsuarioC(),getNombreC(),getApellidoC(),getEmailC(),getCedulaC(),getClaveC(),getLatitudC(),getLongitudC());
                        databaseReference.child(getUsuarioC()).setValue(usuarioAux);
                        Toast.makeText(Registrarse.this,"Usuario Registrado con exito en realtime database",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Registrarse.this,Home.class);
                        startActivity(intent);
                    }
                    else
                    {
                        task.getResult();
                        Toast.makeText(Registrarse.this,task.getResult().toString(),Toast.LENGTH_LONG).show();
//                      Log.i(task.getResult().toString());

                    }
                }
            });
        }
    }

    public void yaTengoCuenta(View v)
    {
        Intent intent = new Intent(v.getContext(), MainActivity.class);
        startActivity(intent);
    }

    public void permiso(){
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Se requiere habilitar los permisos", Toast.LENGTH_SHORT).show();
            }

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    public void escogerImagen(View v) {
        Toast.makeText(v.getContext(), "Galeria", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICKER_REQUEST);
    }
    private void uploadFile(){
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Uri file = Uri.fromFile(new File("path/to/images/image.jpg"));
        StorageReference imageRef = mStorageRef.child("images/profile/userid/image.jpg");
        imageRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
// Get a URL to the uploaded content
                        Log.i("FBApp", "Succesfully upload image");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
// Handle unsuccessful uploads
// ...
                    }
                });
    }


    public String getLatitudC() {
        return latitudC;
    }

    public void setLatitudC(String latitudC) {
        this.latitudC = new String(latitudC);
    }

    public String getLongitudC() {
        return longitudC;
    }

    public void setLongitudC(String longitudC) {
        this.longitudC = new String(longitudC);
    }

    public String getUsuarioC() {
        return usuarioC;
    }

    public void setUsuarioC(String usuarioC) {
        this.usuarioC = usuarioC;
    }

    public String getNombreC() {
        return nombreC;
    }

    public void setNombreC(String nombreC) {
        this.nombreC = nombreC;
    }

    public String getApellidoC() {
        return apellidoC;
    }

    public void setApellidoC(String apellidoC) {
        this.apellidoC = apellidoC;
    }

    public String getEmailC() {
        return emailC;
    }

    public void setEmailC(String emailC) {
        this.emailC = emailC;
    }

    public String getCedulaC() {
        return cedulaC;
    }

    public void setCedulaC(String cedulaC) {
        this.cedulaC = cedulaC;
    }

    public String getClaveC() {
        return claveC;
    }

    public void setClaveC(String claveC) {
        this.claveC = claveC;
    }

    public String getRepetirClaveC() {
        return repetirClaveC;
    }

    public void setRepetirClaveC(String repetirClaveC) {
        this.repetirClaveC = repetirClaveC;
    }
}
