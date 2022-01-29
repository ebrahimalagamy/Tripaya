package com.example.tripaya.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tripaya.roomdatabase.TripClass;
import com.example.tripaya.roomdatabase.TripRepository;

import java.util.List;

// create viewModel to connect between Repo and user interface
// and hold liveData
public class TripViewModel extends AndroidViewModel {
    private TripRepository tripRepository;
    private LiveData<List<TripClass>> allTrips;

    public TripViewModel(@NonNull Application application) {
        super(application);
        tripRepository = new TripRepository(application);
        allTrips = tripRepository.getAllTrips();
    }
    // user interface deal with viewModel not Repo so that from Repo object
    // will arrive to all operation
    public void insert(TripClass tripClass) {
        tripRepository.insert(tripClass);
    }
    public void update(TripClass tripClass) {
        tripRepository.update(tripClass);
    }
    public void delete(TripClass tripClass) {
        tripRepository.delete(tripClass);
    }
    public void deleteAllTrips() {
        tripRepository.deleteAllTrips();
    }
    public LiveData<List<TripClass>> getAllTrips() {
        return allTrips;
    }
    public void setFireBaseWords() {
        tripRepository.getFireData();
    }
}
