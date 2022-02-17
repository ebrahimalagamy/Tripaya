package com.example.tripaya.Alert;

import static com.example.tripaya.Alert.WorkManagerRepo.tripId;

import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.tripaya.R;
import com.example.tripaya.roomdatabase.TripClass;
import com.example.tripaya.viewmodel.TripViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AlertActivity extends AppCompatActivity {


    Button btnStart, btnLater, btnCancel;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;
    TripClass tripClass;
    NotificationManagerCompat notificationManagerCompat;
    Notification notification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.notification);
        mediaPlayer.start();
        btnStart = findViewById(R.id.startBtn);
        btnLater = findViewById(R.id.laterBtn);
        btnCancel = findViewById(R.id.cancelBtn);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        String userUid = mAuth.getUid();


        firebaseDatabase.getReference("Trips").child(userUid).child("UserTrips").
                child(getIntent().getIntExtra(tripId, 0) + "").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tripClass = snapshot.getValue(TripClass.class);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        btnLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushNotification();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "cancel doalog", Toast.LENGTH_SHORT).show();
                tripClass.setTripStatus("Cancel");
            //    tripViewModel.update(tripClass);

            }
        });

        btnStart.setOnClickListener(v -> {
            String sSource = tripClass.getStartPoint();
            Log.i("TAG", "onCreate: "+sSource);
            String sDestination = tripClass.getEndPoint();
            Log.i("TAG", "onCreate: "+sDestination);

            if (sSource.equals("") && sDestination.equals("")) {
                Toast.makeText(v.getContext(), "Please Enter your start and end location", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Uri uri = Uri.parse("http://www.google.co.in/maps/dir/" + sSource + "/" + sDestination);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setPackage("com.google.android.apps.maps");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                } catch (ActivityNotFoundException e) {

                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            }
        });


    }
    public void pushNotification(){

        Intent intent = new Intent(getApplicationContext(), AlertActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"noti")
                .setSmallIcon(android.R.drawable.stat_notify_sync)
                .setContentTitle("Notification")
                .setContentText("Waiting for Trip").setOngoing(true).setAutoCancel(true).setContentIntent(pendingIntent);
        notification=builder.build();
        notificationManagerCompat= NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1,notification);

    }
}