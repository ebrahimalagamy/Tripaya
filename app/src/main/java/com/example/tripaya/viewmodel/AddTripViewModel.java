package com.example.tripaya.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.tripaya.roomdatabase.TripClass;
import com.example.tripaya.roomdatabase.TripRepository;

public class AddTripViewModel extends AndroidViewModel {
    private TripRepository tripRepository;


    public AddTripViewModel(@NonNull Application application) {
        super(application);
        tripRepository = new TripRepository(application);
    }

    // user interface deal with viewModel not Repo so that from Repo object
    // will arrive to all operation

    public void insert(TripClass tripClass) {
        tripRepository.insert(tripClass);
    }

    public void update(TripClass tripClass) {
        tripRepository.update(tripClass);
    }


}
