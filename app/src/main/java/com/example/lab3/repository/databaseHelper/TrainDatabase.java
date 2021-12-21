package com.example.lab3.repository.databaseHelper;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.lab3.model.Train;

@TypeConverters({TrainTypeConverter.class})
@Database(entities = {Train.class}, version = 1, exportSchema = false)
public abstract class TrainDatabase extends RoomDatabase {
    public abstract TrainsDao trainsDao();
}
