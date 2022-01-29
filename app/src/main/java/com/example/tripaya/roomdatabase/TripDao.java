package com.example.tripaya.roomdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// create dao interface for every operation will make it in database
@Dao
public interface TripDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TripClass tripClass);

    @Update
    void update(TripClass tripClass);

    @Delete
    void delete(TripClass tripClass);

    // to delete every TripClass
    @Query("DELETE FROM trip_table")
    void deleteAllTrips();

    // to select every columns and show it in recycler when the app closed
    // use liveDate to observing data if make any change in table and our activity will be notify
    @Query("SELECT * FROM trip_table")
        LiveData<List<TripClass>> getAllTrips();

    @Query("SELECT * FROM trip_table WHERE tripStatus = 'Upcoming' ")
    LiveData<List<TripClass>> getAllNewTrips();

    @Query("SELECT * FROM trip_table WHERE tripStatus != 'Upcoming' ")
    LiveData<List<TripClass>> getAllTripsCompleted();

    @Query("SELECT * FROM trip_table")
    List<TripClass> getTrips();
}
