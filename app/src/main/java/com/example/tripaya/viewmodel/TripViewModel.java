package com.example.tripaya.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tripaya.roomdatabase.TripClass;
import com.example.tripaya.roomdatabase.TripRepository;

import java.util.List;

// create viewModel to connect between Repo and user interface
// and hold liveData
public class TripViewModel extends AndroidViewModel {
    private final TripRepository tripRepository;
    private final LiveData<List<TripClass>> allTrips;
    private final LiveData<List<TripClass>> allzTrips;

    private final LiveData<List<TripClass>> completedTrips;
    private final LiveData<List<TripClass>> historyTrips;
    private final MutableLiveData<Boolean> isSignedOut;


    public TripViewModel(@NonNull Application application) {
        super(application);
        tripRepository = new TripRepository(application);
        allTrips = tripRepository.getAllTrips();
        allzTrips = tripRepository.getAllzTrips();
        completedTrips = tripRepository.getAllCompletedTrips();
        historyTrips = tripRepository.getAllTripsCompleted();
        isSignedOut = new MutableLiveData<Boolean>();
        isSignedOut.postValue(false);
    }

    public MutableLiveData<Boolean> getIsSignedOut() {
        return isSignedOut;
    }

    public void addIsSignedOut(Boolean act) {
        isSignedOut.postValue(act);
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

    public void deleteAllTripsall() {
        tripRepository.deleteAllTripsall();
    }

    public LiveData<List<TripClass>> getAllTrips() {
        return allTrips;
    }

    public LiveData<List<TripClass>> getAllzTrips() {
        return allzTrips;
    }

    public LiveData<List<TripClass>> getCompletedTrips() {
        return completedTrips;
    }

    public LiveData<List<TripClass>> getHistoryTrips() {
        return historyTrips;
    }

    public void setFireBaseTrips() {
        tripRepository.getFireData();
    }
}
