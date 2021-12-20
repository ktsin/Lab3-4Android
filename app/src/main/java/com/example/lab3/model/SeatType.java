package com.example.lab3.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public enum SeatType implements Serializable {
    COMPARTMENT("Compartment"),
    LUXURY("Luxury"),
    RESERVED_SEAT("Reserved seats"),
    SHARED("Shared");

    SeatType(String name) {
        this.name = name;
    }

    public final String name;

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
