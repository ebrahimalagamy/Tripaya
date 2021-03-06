package com.example.tripaya.roomdatabase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {TripClass.class}, version = 6)
public abstract class TripDatabase extends RoomDatabase {

    // create singleton class for database
    // so that i can't create multiple instance of this class
    private static TripDatabase instance;
    // set initial data in database
    // onCreate call in first time user install the app in his device and the database created
    // onOpen call every time user use app
    private static final Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new InitialDBAsyncTask(instance).execute();
        }
    };

    // singleton
    // synchronized that means only one thread can access this method at a time
    // because we don't need create multiple instance if we used multiple threads
    public static synchronized TripDatabase getInstance(Context context) {
        if (instance == null) {
            // here we don't use new to create new instance we use method of room
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TripDatabase.class, "trip_database")
                    .addCallback(callback)
                    // use if we create new version of database this delete old version
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    // to connect roomDatabase with dao
    public abstract TripDao tripDao();

    public static class InitialDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private final TripDao tripDao;

        public InitialDBAsyncTask(TripDatabase database) {

            tripDao = database.tripDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
        /*    tripDao.insert(new TripClass("mansoura","zagaiag","mansoura",
                    "15-10","12.30","round","Upcoming","No notes"));
            tripDao.insert(new TripClass("salhia","zagaiag","mansoura",
                    "15-10","12.30","round","Upcoming","No notes"));
            tripDao.insert(new TripClass("cairo","zagaiag","mansoura",
                    "15-10","12.30","round","Upcoming","No notes"));*/
            return null;
        }
    }
}
