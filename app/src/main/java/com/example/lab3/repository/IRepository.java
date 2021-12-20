package com.example.lab3.repository;

import android.net.Uri;

import com.example.lab3.model.Train;

import java.time.OffsetTime;
import java.util.ArrayList;

public interface IRepository {
    boolean open(Uri uri);
    boolean close();
    boolean save();
    void refresh();
    ArrayList<Train> getAll();
    ArrayList<Train> search(String departurePoint);
    ArrayList<Train> search(String departurePoint, OffsetTime departureTime);
    boolean remove(Train train);
    boolean add(Train train);
    boolean update(Train old, Train newTrain);
    boolean isOpened();
    Train getById(int id);
}
