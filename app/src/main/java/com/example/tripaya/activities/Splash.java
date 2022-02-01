package com.example.tripaya.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tripaya.MainActivity;
import com.example.tripaya.R;
import com.example.tripaya.viewmodel.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Animation topAnimantion, bottomAnimation;
    TextView logo, slogan;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        topAnimantion = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        logo = findViewById(R.id.textView);
        slogan = findViewById(R.id.trips);
        image = findViewById(R.id.image);
        image.setAnimation(topAnimantion);
        logo.setAnimation(bottomAnimation);
        slogan.setAnimation(bottomAnimation);

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
        }, secondsDelayed * 2000);
    }


    public void startLogin() {
        Intent intent = new Intent(Splash.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void startHome() {
        Intent intent = new Intent(Splash.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}