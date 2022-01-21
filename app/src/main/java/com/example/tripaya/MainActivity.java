package com.example.tripaya;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.tripaya.fragments.AddTripFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fabUpcomingFragment;
    private ImageButton imageButtonHistory, imageButtonProfile;
    private int place_word, place_exam;
    private NavController navController;
    RelativeLayout coordinatorLayout;

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
        place_word = 1;
        imageButtonHistory.setOnClickListener(v -> {
            if (place_word == 1) {
                YoYo.with(Techniques.RotateOut)
                        .duration(500)
                        .onEnd(animator -> {
                            fabUpcomingFragment.setImageResource(R.drawable.home);
                            YoYo.with(Techniques.RotateIn)
                                    .duration(500).playOn(fabUpcomingFragment);
                        })
                        .playOn(fabUpcomingFragment);
            }

            place_word = 0;
            if (place_exam == 0) {
                place_exam = 1;
                YoYo.with(Techniques.FadeOut)
                        .duration(400)
                        .onEnd(animator -> {
                            imageButtonHistory.setImageResource(R.drawable.history);
                            YoYo.with(Techniques.BounceInUp)
                                    .duration(500).playOn(imageButtonHistory);
                        }).playOn(imageButtonHistory);

                navController.navigate(R.id.historyFragment);

            }
        });
        fabUpcomingFragment.setOnClickListener(v -> {
            if (place_word == 0) {
                navController.navigate(R.id.upcomingFragment);
                place_word = 1;

                YoYo.with(Techniques.RotateOut)
                        .duration(700)
                        .onEnd(animator -> {
                            fabUpcomingFragment.setImageResource(R.drawable.ic_baseline_add);
                            YoYo.with(Techniques.RotateIn)
                                    .duration(700).playOn(fabUpcomingFragment);
                        })
                        .playOn(fabUpcomingFragment);

            } else if (place_word == 1) {
                Intent i = new Intent(MainActivity.this, AddTripActivity.class);
                startActivity(i);
            }
            if (place_exam == 1) {
                place_exam = 0;
                YoYo.with(Techniques.FadeOut)
                        .duration(400)
                        .onEnd(animator -> {
                            imageButtonHistory.setImageResource(R.drawable.history);
                            YoYo.with(Techniques.BounceInUp)
                                    .duration(500).playOn(imageButtonHistory);
                        }).playOn(imageButtonHistory);
            }


        });
        // TODO will fix the image transaction
        imageButtonProfile.setOnClickListener(v -> {
            if (place_exam == 1) {
                place_exam = 0;
                YoYo.with(Techniques.FadeOut)
                        .duration(400)
                        .onEnd(animator -> {
                            imageButtonHistory.setImageResource(R.drawable.history);
                            YoYo.with(Techniques.BounceInUp)
                                    .duration(500).playOn(imageButtonHistory);
                        }).playOn(imageButtonHistory);
            }

            if (place_word == 1) {
                YoYo.with(Techniques.RotateOut)
                        .duration(500)
                        .onEnd(animator -> {
                            fabUpcomingFragment.setImageResource(R.drawable.home);
                            YoYo.with(Techniques.RotateIn)
                                    .duration(500).playOn(fabUpcomingFragment);
                        })
                        .playOn(fabUpcomingFragment);

            }
            place_word = 0;
            navController.navigate(R.id.profileFragment);

        });

    }

    private void initComponent() {
        // use fab to switch to upcoming fragment and add new trip
        fabUpcomingFragment = findViewById(R.id.fab_button);
        imageButtonHistory = findViewById(R.id.image_button_history);
        imageButtonProfile = findViewById(R.id.image_button_profile);
        coordinatorLayout= findViewById(R.id.gone);
    }
}