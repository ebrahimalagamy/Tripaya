package com.example.tripaya.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.tripaya.Authentication.Login;
import com.example.tripaya.MainActivity;
import com.example.tripaya.R;
import com.example.tripaya.viewmodel.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                mUser = FirebaseAuth.getInstance().getCurrentUser();
                if (mUser != null) {
                    startHome();
                } else {
                    startLogin();
                }

            }
        }, secondsDelayed * 1000);
    }



    public void startLogin(){
        Intent intent = new Intent(Splash.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void startHome(){
            Intent intent = new Intent(Splash.this, MainActivity.class);
            startActivity(intent);
            finish();
    }
}