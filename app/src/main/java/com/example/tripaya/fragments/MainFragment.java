package com.example.tripaya.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.tripaya.AddTripActivity;
import com.example.tripaya.MainActivity;
import com.example.tripaya.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainFragment extends Fragment {
    private FloatingActionButton fabUpcomingFragment;
    private ImageButton imageButtonHistory, imageButtonProfile;
    private NavController navController;
    private int upcomingSwitch, historySwitch, profileSwitch;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view= inflater.inflate(R.layout.fragment_main, container, false);
        initComponent();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_2);
        navController = navHostFragment.getNavController();

        initViewsListener();
    }

    private void initComponent() {
        // use fab to switch to upcoming fragment and add new trip
        fabUpcomingFragment = view.findViewById(R.id.fab_button);
        imageButtonHistory = view.findViewById(R.id.image_button_history);
        imageButtonProfile = view.findViewById(R.id.image_button_profile);
    }

    private void initViewsListener() {
        upcomingSwitch = 0;
        historySwitch = 0;
        profileSwitch = 0;

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
                Intent i = new Intent(getActivity(), AddTripActivity.class);
                startActivity(i);
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
}