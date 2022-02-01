package com.example.tripaya.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tripaya.roomdatabase.TripClass;
import com.example.tripaya.roomdatabase.TripRepository;

import java.util.List;

public class AddTripViewModel extends AndroidViewModel {
    private final TripRepository tripRepository;
    private final LiveData<List<TripClass>> allTrips;

    private final LiveData<List<TripClass>> allzTrips;


    public AddTripViewModel(@NonNull Application application) {
        super(application);
        tripRepository = new TripRepository(application);
        allTrips = tripRepository.getAllTrips();
        allzTrips = tripRepository.getAllzTrips();

    }

    // user interface deal with viewModel not Repo so that from Repo object
    // will arrive to all operation

    public void insert(TripClass tripClass) {
        tripRepository.insert(tripClass);
    }

    public void update(TripClass tripClass) {
        tripRepository.update(tripClass);
    }

    public LiveData<List<TripClass>> getAllTrips() {
        return allTrips;
    }

    public LiveData<List<TripClass>> getAllzTrips() {
        return allzTrips;
    }

}
