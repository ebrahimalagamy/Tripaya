package com.example.tripaya.roomdatabase;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

// fetch data room room / we service and liveDate notify viewModel
public class TripRepository {
    private TripDao tripDao ;
    private LiveData<List<TripClass>> getAllTrips;

    // in view model will pass application and application is subclass of context
    public TripRepository(Application application){
        TripDatabase tripDatabase = TripDatabase.getInstance(application);

        // tripDao() is abstract in TripDatabase class
        tripDao = tripDatabase.tripDao();
        getAllTrips = tripDao.getAllTrips();
    }

    // write operation in another thread
    public void insert(TripClass tripClass){
        new InsertTripAsyncTask(tripDao).execute(tripClass);
    }
    public void update(TripClass tripClass){
        new UpdateTripAsyncTask(tripDao).execute(tripClass);
    }
    public void delete(TripClass tripClass){
        new DeleteTripAsyncTask(tripDao).execute(tripClass);
    }
    public void deleteAllTrips(){
        new DeleteAllTripAsyncTask(tripDao).execute();
    }

    public LiveData<List<TripClass>> getAllTrips() {
        return getAllTrips;
    }

    private static class InsertTripAsyncTask extends AsyncTask<TripClass,Void,Void>{

        //need dao to make database operation
        private TripDao tripDao;
        public InsertTripAsyncTask(TripDao tripDao){
            this.tripDao = tripDao;
        }
        @Override
        protected Void doInBackground(TripClass... tripClasses) {
            tripDao.insert(tripClasses[0]);
            return null;
        }


    }
    private static class UpdateTripAsyncTask extends AsyncTask<TripClass,Void,Void>{

        //need dao to make database operation
        private TripDao tripDao;
        public UpdateTripAsyncTask(TripDao tripDao){
            this.tripDao = tripDao;
        }
        @Override
        protected Void doInBackground(TripClass... tripClasses) {
            tripDao.update(tripClasses[0]);
            return null;
        }


    }
    private static class DeleteTripAsyncTask extends AsyncTask<TripClass,Void,Void>{

        //need dao to make database operation
        private TripDao tripDao;
        public DeleteTripAsyncTask(TripDao tripDao){
            this.tripDao = tripDao;
        }
        @Override
        protected Void doInBackground(TripClass... tripClasses) {
            tripDao.delete(tripClasses[0]);
            return null;
        }


    }
    private static class DeleteAllTripAsyncTask extends AsyncTask<Void,Void,Void>{

        //need dao to make database operation
        private TripDao tripDao;
        public DeleteAllTripAsyncTask(TripDao tripDao){
            this.tripDao = tripDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            tripDao.deleteAllTrips();
            return null;
        }


    }

}
