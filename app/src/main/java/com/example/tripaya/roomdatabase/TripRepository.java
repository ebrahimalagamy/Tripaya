package com.example.tripaya.roomdatabase;


import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tripaya.Alert.WorkManagerRepo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
// fetch data room room / we service and liveDate notify viewModel

public class TripRepository {
    private TripDao tripDao;
    private LiveData<List<TripClass>> getAllTrips;
    private LiveData<List<TripClass>> getAllTripsCompleted;
    private List<TripClass> getTrips;
    private final MutableLiveData<List<TripClass>> firebaseTrips;
    public int size = 0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Trips");
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String Uid = firebaseAuth.getCurrentUser().getUid();

    // in view model will pass application and application is subclass of context
    public TripRepository(Application application) {
        TripDatabase tripDatabase = TripDatabase.getInstance(application);
        // tripDao() is abstract in TripDatabase class
        tripDao = tripDatabase.tripDao();
        getAllTrips = tripDao.getAllNewTrips();
        getAllTripsCompleted = tripDao.getAllTripsCompleted();
        // getTrips = tripDao.getTrips();
        firebaseTrips = new MutableLiveData<>();
        FireBase();
    }

    private void FireBase() {
        myRef.child(Uid).child("UserTrips").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<TripClass> list = new ArrayList<>();

                if (dataSnapshot.exists())
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        list.add(snapshot.getValue(TripClass.class));
                    }
                firebaseTrips.setValue(list);
                size = firebaseTrips.getValue().size();
                getFirebaseTrips();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // write operation in another thread
    public void insert(TripClass tripClass) {
        new InsertTripAsyncTask(tripDao).execute(tripClass);
        myRef.child(Uid).child("UserTrips").child(String.valueOf(tripClass.getId())).setValue(tripClass);

    }

    public void insert2(TripClass tripClass) {
        new InsertTripAsyncTask(tripDao).execute(tripClass);
    }

    public void update(TripClass tripClass) {
        new UpdateTripAsyncTask(tripDao).execute(tripClass);
        myRef.child(Uid).child("UserTrips").child(String.valueOf(tripClass.getId())).setValue(tripClass);
    }

    public void delete(TripClass tripClass) {
        new DeleteTripAsyncTask(tripDao).execute(tripClass);
        myRef.child(Uid).child("UserTrips").child(String.valueOf(tripClass.getId())).removeValue();

    }

    public void deleteAllTrips() {
        new DeleteAllTripAsyncTask(tripDao).execute();
        myRef.child(Uid).child("UserTrips").removeValue();
    }

    // use it to return firebase words;
    public void getFireData() {
        myRef.child(Uid).child("UserTrips").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<TripClass> fireword = new ArrayList<>();
                //       allWords = wordsDao.getAllWords();
                //if (allWords.getValue() != null)
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        TripClass word = dataSnapshot1.getValue(TripClass.class);
                        fireword.add(word);
                        // FireList.add(word);
                    }
                    Set<TripClass> set = new LinkedHashSet<>();
                    set.addAll(fireword);
                    fireword.clear();
                    fireword.addAll(set);
                    for (int i = 0; i < fireword.size(); i++) {
                        insert2(fireword.get(i));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getFireDataCompleted() {
        myRef.child(Uid).child("UserTrips").child("tripStatus").equalTo("cancel")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<TripClass> trip = new ArrayList<>();
                        //       allWords = wordsDao.getAllWords();
                        //if (allWords.getValue() != null)
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                TripClass tripClass = dataSnapshot1.getValue(TripClass.class);
                                trip.add(tripClass);
                                // FireList.add(word);
                            }
                            Set<TripClass> set = new LinkedHashSet<>();
                            set.addAll(trip);
                            trip.clear();
                            trip.addAll(set);
                            for (int i = 0; i < trip.size(); i++) {
                                insert2(trip.get(i));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    public LiveData<List<TripClass>> getAllTrips() {
        return getAllTrips;
    }

    public LiveData<List<TripClass>> getAllTripsCompleted() {
        return getAllTripsCompleted;
    }

    public List<TripClass> getTrips() {
        return getTrips;
    }

    public LiveData<List<TripClass>> getFirebaseTrips() {
        return firebaseTrips;

    }

    private static class InsertTripAsyncTask extends AsyncTask<TripClass, Void, Void> {

        //need dao to make database operation
        private TripDao tripDao;

        public InsertTripAsyncTask(TripDao tripDao) {
            this.tripDao = tripDao;
        }

        @Override
        protected Void doInBackground(TripClass... tripClasses) {
            tripDao.insert(tripClasses[0]);
            return null;
        }


    }

    private static class UpdateTripAsyncTask extends AsyncTask<TripClass, Void, Void> {

        //need dao to make database operation
        private TripDao tripDao;

        public UpdateTripAsyncTask(TripDao tripDao) {
            this.tripDao = tripDao;
        }

        @Override
        protected Void doInBackground(TripClass... tripClasses) {
            tripDao.update(tripClasses[0]);
            return null;
        }


    }

    private static class DeleteTripAsyncTask extends AsyncTask<TripClass, Void, Void> {

        //need dao to make database operation
        private TripDao tripDao;

        public DeleteTripAsyncTask(TripDao tripDao) {
            this.tripDao = tripDao;
        }

        @Override
        protected Void doInBackground(TripClass... tripClasses) {
            tripDao.delete(tripClasses[0]);
            return null;
        }

    }

    private static class DeleteAllTripAsyncTask extends AsyncTask<Void, Void, Void> {

        //need dao to make database operation
        private TripDao tripDao;

        public DeleteAllTripAsyncTask(TripDao tripDao) {
            this.tripDao = tripDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            tripDao.deleteAllTrips();
            return null;
        }


    }


}
