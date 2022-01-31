package com.example.tripaya.Alert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tripaya.R;
import com.example.tripaya.adapter.TripAdapter;
import com.example.tripaya.roomdatabase.TripClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AlertActivity extends AppCompatActivity {

    Button btnStart,btnLater,btnCancel;
    TripClass trips;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;
    TripClass tripClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        btnStart = findViewById(R.id.startBtn);
        btnLater = findViewById(R.id.laterBtn);
        btnCancel = findViewById(R.id.cancelBtn);

        String userUid = mAuth.getUid();
        firebaseDatabase.getReference("Trips").child(userUid).child("UserTrips").child(trips.getId()+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 tripClass = snapshot.getValue(TripClass.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sSource = tripClass.getStartPoint();
                String sDestination = tripClass.getEndPoint();

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
            }
        });


    }
}