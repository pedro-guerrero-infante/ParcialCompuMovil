package com.example.taller2;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.taller2.Auxiliares.UsuarioAux;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    //---------------------------------------------------------------------------------------
    private StorageReference storageReference;
    private Uri imagenUri;
    private ImageView imagen;
    //---------------------------------------------------------------------------------------
    private Boolean bandera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        sacarUbicacion();

        imagen = findViewById(R.id.imagen);
        storageReference = FirebaseStorage.getInstance().getReference();
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

    //-----------------------------------------------------------------------------------------------------------

    public void cargarImagen(View v)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String [] permiso = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permiso,1001);
            }
            else{
                escogerImagenGaleria();
            }
        }
        else {
            escogerImagenGaleria();
        }
    }

    private void escogerImagenGaleria() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,1000);

    }

    //Este codigo es para seleccionar la imagen y que salga en la pantalla
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1000) {

            imagenUri = data.getData();
            imagen.setImageURI(imagenUri);
        }
        else if(requestCode == 1 && resultCode == RESULT_OK && data !=null){
            Bundle bundle = data.getExtras();

            Bitmap foto = (Bitmap) bundle.get("data");
            imagen.setImageBitmap(foto);
        }
    }

    //-----------------------------------------------------------------------------------------------------------

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
        setBandera(true);

        if (getUsuarioC().isEmpty())
        {
            setBandera(false);
            mostrarError(usuario,"Debes llenar este campo");
        }
        if (getNombreC().isEmpty())
        {
            setBandera(false);
            mostrarError(nombre,"Debes llenar este campo");
        }
        if (getApellidoC().isEmpty())
        {
            setBandera(false);
            mostrarError(apellido,"Debes llenar este campo");
        }
        if (getEmailC().isEmpty())
        {
            setBandera(false);
            mostrarError(email,"Debes llenar este campo");
        }
        if (getCedulaC().isEmpty())
        {
            setBandera(false);
            mostrarError(cedula,"Debes llenar este campo");
        }
        if (getClaveC().isEmpty())
        {
            setBandera(false);
            mostrarError(clave,"Debes llenar este campo");
        }
        if (getRepetirClaveC().isEmpty())
        {
            setBandera(false);
            mostrarError(repetirClave,"Debes llenar este campo");
        }
        else
        {
            if(!getClaveC().equals(getRepetirClaveC()))
            {
                setBandera(false);
                mostrarError(repetirClave,"No son iguales las contraseñas");
            }
        }
        if(imagenUri == null)
        {
            setBandera(false);
            Toast.makeText(Registrarse.this,"Porfa Selecciona una imagen...",Toast.LENGTH_LONG).show();
        }

        if(getBandera())
        {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(emailC,claveC);

            StorageReference archivo = storageReference.child(getUsuarioC()).child("ImagenPerfil");
            archivo.putFile(imagenUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    archivo.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            databaseReference = firebaseDatabase.getReference("Usuarios");
                            UsuarioAux usuarioAux = new UsuarioAux( getUsuarioC(),
                                                                    getNombreC(),
                                                                    getApellidoC(),
                                                                    getEmailC(),
                                                                    getCedulaC(),
                                                                    getClaveC(),
                                                                    getLatitudC(),
                                                                    getLongitudC(),
                                                                    uri.toString());

                            databaseReference.child(getUsuarioC()).setValue(usuarioAux);
                            Toast.makeText(Registrarse.this,"Usuario Registrado",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Registrarse.this,Home.class);
                            startActivity(intent);
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                progressBar.setVisibility(View.VISIBLE);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Registrarse.this,"Error....",Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void yaTengoCuenta(View v)
    {
        Intent intent = new Intent(v.getContext(), MainActivity.class);
        startActivity(intent);
    }

    //----------------------------------------------------------------------------------------------
    // Getters y Setters
    //----------------------------------------------------------------------------------------------

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

    public Boolean getBandera() {
        return bandera;
    }

    public void setBandera(Boolean bandera) {
        this.bandera = bandera;
    }
}
