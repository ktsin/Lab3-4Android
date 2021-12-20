package com.example.lab3.repository.databaseHelper;

import androidx.room.TypeConverter;

import com.example.lab3.model.Seat;
import com.example.lab3.model.Train;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TrainTypeConverter {
    private static final Gson gson;

    static {
        gson = new Gson();
    }

    @TypeConverter
    public static String seatsToJson(ArrayList<Seat> seats){
        Type collectionType = new TypeToken<ArrayList<Train>>() {}.getType();
        return gson.toJson(seats, collectionType);
    }

    @TypeConverter
    public static ArrayList<Seat> jsonToSeats(String seats){
        Type collectionType = new TypeToken<ArrayList<Train>>() {}.getType();
        return gson.fromJson(seats, collectionType);
    }
}
