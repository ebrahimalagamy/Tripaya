package com.example.tripaya.Alert;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.tripaya.roomdatabase.TripClass;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WorkManagerRepo {
    public static final String tripId = "TripId";

    public static void setWorkers(Context context, List<TripClass> trips) {
        WorkManager.getInstance(context).cancelAllWork();

        for (TripClass trip : trips) {

            Data.Builder builder = new Data.Builder();

            builder.putInt(tripId,trip.getId());
            System.out.println("new Date().getTime() - trip.getStartDate().getTime()");
            Log.e(" Date().getTime()", String.valueOf(trip.dateFromStringToDate().getTime()-new Date().getTime()));
            WorkRequest oneTimeWorkRequest = new OneTimeWorkRequest
                    .Builder(TripWorker.class)
                    .setInitialDelay(
                            trip.dateFromStringToDate().getTime()-new Date().getTime(),
                            TimeUnit.MILLISECONDS
                    )
                    .setInputData(builder.build())
                    .build();

            WorkManager.getInstance(context).enqueue(oneTimeWorkRequest);

        }
    }


    public static class TripWorker extends Worker {
        Context context;

        public TripWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
            this.context = context;
        }

        @NonNull
        @Override
        public Result doWork() {
//            TripCRUD.getInstance().getAll();

            Intent intent = new Intent(context, AlertActivity.class);
            String trip = getInputData().getString(tripId);

            System.out.println("trip");
            System.out.println(trip);

            intent.putExtra(tripId,trip)
                    .addFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            return Result.success();
        }
    }
}
