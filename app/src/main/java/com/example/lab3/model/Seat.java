package com.example.lab3.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Seat implements Serializable {
    private SeatType seatType;
    private int count;

    public Seat(SeatType seatType, int count) {
        this.seatType = seatType;
        this.count = count;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @NonNull
    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("%s: %d", seatType.toString(), count);
    }
}
