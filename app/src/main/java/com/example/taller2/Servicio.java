package com.example.taller2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.taller2.Auxiliares.UsuarioAux;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Servicio extends JobIntentService {
    private static final int JOB_ID = 12;
    private static String CHANNEL_ID = "Notificacion";
    private int notificationId = 150;
    private static String idUsuarioDestino;

    public static void enqueueWork(Context context, Intent intent, String idUsuarioDestinoP){
        setIdUsuarioDestino(idUsuarioDestinoP);
        System.out.println("Codigo en el enqueueWork: "+getIdUsuarioDestino());
        enqueueWork(context, Servicio.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        try {
                createNotificationChannel();
                crearNotificacion();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private void createNotificationChannel(){
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notificacion";
            //String description = "Nueva notificacion";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            //IMPORTANCE_MAX MUESTRA LA NOTIFICACIÓN ANIMADA
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            // channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void crearNotificacion(){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.noticon);
        mBuilder.setContentTitle("Tienes un nueva notificacion");
        mBuilder.setContentText("Un usuario nuevo esta activo. Mira su ubicacion!!");
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        mBuilder.setLights(Color.MAGENTA, 1000, 1000);
        mBuilder.setVibrate(new long[] {1000, 1000, 1000, 1000, 1000});

        Intent primerIntent = new Intent(this, Home.class);
        System.out.println("Codigo dentro de la notificacion "+getIdUsuarioDestino());
        primerIntent.putExtra("codigoUsuarioX2",getIdUsuarioDestino());

        primerIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent peIntent = PendingIntent.getActivity(this, notificationId, primerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(peIntent);
        mBuilder.setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, mBuilder.build());
    }

    public static String getIdUsuarioDestino() {
        return idUsuarioDestino;
    }

    public static void setIdUsuarioDestino(String idUsuarioDestino) {
        Servicio.idUsuarioDestino = idUsuarioDestino;
    }
}
