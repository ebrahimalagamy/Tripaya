package com.example.tripaya.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tripaya.roomdatabase.TripClass;
import com.example.tripaya.roomdatabase.TripRepository;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private final TripRepository tripRepository;
    private final LiveData<List<TripClass>> allTripsCompleted;


    public HistoryViewModel(@NonNull Application application) {
        super(application);
        tripRepository = new TripRepository(application);
        allTripsCompleted = tripRepository.getAllTripsCompleted();

    }

    // user interface deal with viewModel not Repo so that from Repo object
    // will arrive to all operation

    public void insert(TripClass tripClass) {
        tripRepository.insert(tripClass);
    }

    public void delete(TripClass tripClass) {
        tripRepository.delete(tripClass);
    }

    public LiveData<List<TripClass>> getAllTripsCompleted() {
        return allTripsCompleted;
    }

    public void setFireCompleted() {
        tripRepository.getFireDataCompleted();
    }

}
