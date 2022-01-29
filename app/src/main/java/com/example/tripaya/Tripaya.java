package com.example.tripaya;

import android.app.Application;

import androidx.multidex.MultiDex;

import com.google.firebase.database.FirebaseDatabase;

public class Tripaya extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
