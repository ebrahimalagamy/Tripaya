package com.example.tripaya;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabUpcomingFragment;
    private ImageButton imageButtonHistory, imageButtonProfile;
    private NavController navController;
    private int upcomingSwitch, historySwitch, profileSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        initViewsListener();
    }

    private void initViewsListener() {
        upcomingSwitch = historySwitch = profileSwitch= 0;

        imageButtonHistory.setOnClickListener(v -> {
            if (historySwitch == 0) {
                historySwitch = 1;

                YoYo.with(Techniques.FadeOut)
                        .duration(400)
                        .onEnd(animator -> {
                            imageButtonHistory.setImageResource(R.drawable.history);
                            YoYo.with(Techniques.BounceInUp)
                                    .duration(500).playOn(imageButtonHistory);
                        }).playOn(imageButtonHistory);
                // switch to history fragment
                navController.navigate(R.id.historyFragment);

            }
            if (upcomingSwitch == 0) {
                upcomingSwitch = 1;
                YoYo.with(Techniques.RotateOut)
                        .duration(500)
                        .onEnd(animator -> {
                            fabUpcomingFragment.setImageResource(R.drawable.home);
                            YoYo.with(Techniques.RotateIn)
                                    .duration(500).playOn(fabUpcomingFragment);
                        })
                        .playOn(fabUpcomingFragment);
            }

            if (profileSwitch == 1) {
                profileSwitch = 0;
                YoYo.with(Techniques.FadeOut)
                        .duration(400)
                        .onEnd(animator -> {
                            imageButtonProfile.setImageResource(R.drawable.profile);
                            YoYo.with(Techniques.BounceInUp)
                                    .duration(500).playOn(imageButtonProfile);
                        }).playOn(imageButtonProfile);
            }

        });
        fabUpcomingFragment.setOnClickListener(v -> {
            if (upcomingSwitch == 1) {
                upcomingSwitch = 0;
                YoYo.with(Techniques.RotateOut)
                        .duration(700)
                        .onEnd(animator -> {
                            fabUpcomingFragment.setImageResource(R.drawable.ic_baseline_add);
                            YoYo.with(Techniques.RotateIn)
                                    .duration(700).playOn(fabUpcomingFragment);
                        })
                        .playOn(fabUpcomingFragment);

                // switch to upcoming fragment
                navController.navigate(R.id.upcomingFragment);
            } else if (upcomingSwitch == 0) {
                if (InternetConnection.checkConnection(this)){
                Intent i = new Intent(MainActivity.this, AddTripActivity.class);
                startActivity(i);}
                else{
                    Toast.makeText(MainActivity.this,"plz Check internet connection",Toast.LENGTH_LONG).show();
                }
            }

            if (historySwitch == 1) {
                historySwitch = 0;
                YoYo.with(Techniques.FadeOut)
                        .duration(400)
                        .onEnd(animator -> {
                            imageButtonHistory.setImageResource(R.drawable.history);
                            YoYo.with(Techniques.BounceInUp)
                                    .duration(500).playOn(imageButtonHistory);
                        }).playOn(imageButtonHistory);
            }
            if (profileSwitch == 1) {
                profileSwitch = 0;
                YoYo.with(Techniques.FadeOut)
                        .duration(400)
                        .onEnd(animator -> {
                            imageButtonProfile.setImageResource(R.drawable.profile);
                            YoYo.with(Techniques.BounceInUp)
                                    .duration(500).playOn(imageButtonProfile);
                        }).playOn(imageButtonProfile);

            }

        });
        imageButtonProfile.setOnClickListener(v -> {
            if (profileSwitch == 0) {
                profileSwitch = 1;
                YoYo.with(Techniques.FadeOut)
                        .duration(400)
                        .onEnd(animator -> {
                            imageButtonProfile.setImageResource(R.drawable.profile);
                            YoYo.with(Techniques.BounceInUp)
                                    .duration(500).playOn(imageButtonProfile);
                        }).playOn(imageButtonProfile);

                // switch to profile fragment
                navController.navigate(R.id.profileFragment);
            }

            if (upcomingSwitch == 0) {
                upcomingSwitch = 1;
                YoYo.with(Techniques.RotateOut)
                        .duration(500)
                        .onEnd(animator -> {
                            fabUpcomingFragment.setImageResource(R.drawable.home);
                            YoYo.with(Techniques.RotateIn)
                                    .duration(500).playOn(fabUpcomingFragment);
                        })
                        .playOn(fabUpcomingFragment);

            }
            if (historySwitch == 1) {
                historySwitch = 0;
                YoYo.with(Techniques.FadeOut)
                        .duration(500)
                        .onEnd(animator -> {
                            imageButtonHistory.setImageResource(R.drawable.history);
                            YoYo.with(Techniques.BounceIn)
                                    .duration(500).playOn(imageButtonHistory);
                        })
                        .playOn(imageButtonHistory);
            }

        });
    }

    private void initComponent() {
        // use fab to switch to upcoming fragment and add new trip
        fabUpcomingFragment = findViewById(R.id.fab_button);
        imageButtonHistory = findViewById(R.id.image_button_history);
        imageButtonProfile = findViewById(R.id.image_button_profile);
    }

    
}