package com.example.lab3.repository.databaseHelper;

import androidx.room.TypeConverter;

import com.example.lab3.model.Seat;
import com.example.lab3.model.Train;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TrainTypeConverter {
    private static final Gson gson;

    static {
        gson = new Gson();
    }

    @TypeConverter
    public static String seatsToJson(ArrayList<Seat> seats) {
        if (seats != null) {
            Type collectionType = new TypeToken<ArrayList<Seat>>() {
            }.getType();
            return gson.toJson(seats, collectionType);
        }
        else{
            return "[]";
        }
    }

    @TypeConverter
    public static ArrayList<Seat> jsonToSeats(String seats) {
        if(seats != null){
            Type collectionType = new TypeToken<ArrayList<Seat>>() {
            }.getType();
            return gson.fromJson(seats, collectionType);
        }
        else
            return new ArrayList<Seat>();

    }

    @TypeConverter
    public static String offsetTimeToText(OffsetTime time) {
        if (time != null)
            return time.format(DateTimeFormatter.ISO_OFFSET_TIME);
        else
            return OffsetTime.MIN.format(DateTimeFormatter.ISO_OFFSET_TIME);
    }

    @TypeConverter
    public static OffsetTime textToOffsetTime(String time) {
        if (time != null)
            return OffsetTime.parse(time, DateTimeFormatter.ISO_OFFSET_TIME);
        else
            return OffsetTime.MIN;
    }
}
