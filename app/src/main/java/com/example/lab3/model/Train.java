package com.example.lab3.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.OffsetTime;
import java.util.ArrayList;

@Entity
public class Train implements Serializable {
    @PrimaryKey
    private int id;

    private String destination;
    private String trainNumber;
    private OffsetTime departureTime;
    private ArrayList<Seat> seats;

    public Train(String destination, String trainNumber, OffsetTime departureTime, ArrayList<Seat> seats) {
        this.destination = destination;
        this.trainNumber = trainNumber;
        this.departureTime = departureTime;
        this.seats = seats;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public OffsetTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(OffsetTime departureTime) {
        this.departureTime = departureTime;
    }

    public ArrayList<Seat> getSeats() {
        return seats;
    }

    public void setSeats(ArrayList<Seat> seats) {
        this.seats = seats;
    }

    @NonNull
    @Override
    public String toString() {
        return "Train{" +
                "id='" + id + '\'' +
                "destination='" + destination + '\'' +
                ", trainNumber='" + trainNumber + '\'' +
                ", departureTime=" + departureTime +
                ", seats=" + seats +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
