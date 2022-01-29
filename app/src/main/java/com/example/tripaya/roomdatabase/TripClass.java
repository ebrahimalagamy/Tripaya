package com.example.tripaya.roomdatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "trip_table")
public class TripClass implements Serializable {

    // room create columns for this fields
    // table need primaryKey to be unique
    // for every row creation will take autoGenerate id
    @PrimaryKey(autoGenerate = false)
    private int id;

    private String tripName;
    private String startPoint;
    private String endPoint;
    private String date;
    private String time;
    private String tripType;
    private String tripStatus;
    private String note;
    /*private String roundDate;
    private String roundTime;*/


    public TripClass() {
    }

    public TripClass(String tripName, String startPoint, String endPoint, String date,
                     String time, String tripType, String tripStatus, String note) {
        this.tripName = tripName;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.date = date;
        this.time = time;
        this.tripType = tripType;
        this.tripStatus = tripStatus;
        this.note = note;
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

    public String getTripStatus() {
        return tripStatus;
    }

    public String getNote() {
        return note;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
