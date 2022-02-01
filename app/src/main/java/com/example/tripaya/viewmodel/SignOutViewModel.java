package com.example.tripaya.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.tripaya.roomdatabase.TripRepository;

public class SignOutViewModel extends AndroidViewModel {
    private final TripRepository tripRepository;

    public SignOutViewModel(@NonNull Application application) {
        super(application);
        tripRepository = new TripRepository(application);
    }

    public void deleteAllTrips() {
        tripRepository.deleteAllTrips();
    }
}
