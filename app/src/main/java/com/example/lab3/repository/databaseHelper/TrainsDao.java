package com.example.lab3.repository.databaseHelper;

import android.database.sqlite.SQLiteQuery;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.lab3.model.Train;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TrainsDao {
    @Query("SELECT * FROM train")
    List<Train> getAllTrains();

    @Insert
    void insert(Train train);

    @Update
    void update(Train train);

    @Delete
    void remove(Train train);

    @Query("SELECT * FROM Train WHERE destination LIKE '%' || :destination || '%'")
    List<Train> searchByDestination(String destination);

    @Query("SELECT  * FROM Train WHERE Train.id = :id LIMIT 1")
    Train getById(int id);

    @RawQuery
    int save(SupportSQLiteQuery query);
}
