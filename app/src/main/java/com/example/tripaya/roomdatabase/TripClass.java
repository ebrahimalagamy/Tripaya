package com.example.tripaya.roomdatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "trip_table")
public class TripClass {
    // room create columns for this fields
    // table need primaryKey to be unique
    // for every row creation will take autoGenerate id
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String tripName;
    private String startPoint;
    private String endPoint;
    private String date;
    private String time;
    private String tripType;


    public TripClass(String tripName, String startPoint, String endPoint, String date, String time, String tripType) {
        this.tripName = tripName;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.date = date;
        this.time = time;
        this.tripType = tripType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTripName() {
        return tripName;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTripType() {
        return tripType;
    }
}
