package com.example.lab3.repository;

import android.net.Uri;

import com.example.lab3.model.Train;

import java.time.OffsetTime;
import java.util.ArrayList;

public class DatabaseRepository implements IRepository{
    @Override
    public boolean open(Uri uri) {
        return false;
    }

    @Override
    public boolean close() {
        return false;
    }

    @Override
    public boolean save() {
        return false;
    }

    @Override
    public void refresh() {

    }

    @Override
    public ArrayList<Train> getAll() {
        return null;
    }

    @Override
    public ArrayList<Train> search(String departurePoint) {
        return null;
    }

    @Override
    public ArrayList<Train> search(String departurePoint, OffsetTime departureTime) {
        return null;
    }

    @Override
    public boolean remove(Train train) {
        return false;
    }

    @Override
    public boolean add(Train train) {
        return false;
    }

    @Override
    public boolean update(Train old, Train newTrain) {
        return false;
    }

    @Override
    public boolean isOpened() {
        return false;
    }

    @Override
    public Train getById(int id) {
        return null;
    }
}
