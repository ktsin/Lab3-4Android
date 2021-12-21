package com.example.lab3.repository;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.room.Room;

import com.example.lab3.model.Train;
import com.example.lab3.repository.databaseHelper.TrainDatabase;
import com.example.lab3.services.DatabaseService;

import java.time.OffsetTime;
import java.util.ArrayList;

public class DatabaseRepository implements IRepository{
    private boolean isOpened;
    private DatabaseService service;
    private String dbName;


    @Override
    public boolean open(String uri) {
        boolean status = true;
        dbName = uri;
        try{
            Intent intent = new Intent(RepositoryHolder.getApplicationContext(), DatabaseService.class);
            //RepositoryHolder.getApplicationContext().startService(intent);
            RepositoryHolder.getApplicationContext().bindService(
                    intent, new DatabaseServiceConnection(), Context.BIND_AUTO_CREATE);
        }
        catch (Exception ex){
            Log.e("D015", ex.getMessage(), ex.getCause());
        }
        return false;
    }

    @Override
    public boolean close() {

        return false;
    }

    @Override
    public boolean save() {
        service.checkpoint();
        return true;
    }

    @Override
    public void refresh() {

    }

    @Override
    public ArrayList<Train> getAll() {
        ArrayList<Train> trains = new ArrayList<>();
        if(service != null){
            trains = service.getAll();
        }
        return trains;
    }

    @Override
    public ArrayList<Train> search(String departurePoint) {
        if(service != null){
            return service.search(departurePoint);
        }
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Train> search(String destinationPoint, OffsetTime departureTime) {
        if(service != null){
            return service.search(destinationPoint, departureTime);
        }

        return new ArrayList<>();
    }

    @Override
    public boolean remove(Train train) {
        if(service != null){
            service.remove(train);
        }
        return false;
    }

    @Override
    public boolean add(Train train) {
        if(service != null){
            service.add(train);
        }
        return false;
    }

    @Override
    public boolean update(Train old, Train newTrain) {
        if (service != null){
            service.update(newTrain);
        }
        return false;
    }

    @Override
    public boolean isOpened() {
        return isOpened;
    }

    @Override
    public Train getById(int id) {
        if(service != null){
            return service.getById(id);
        }
        return null;
    }

    private class DatabaseServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if(service instanceof DatabaseService.DatabaseServiceBinder){
                DatabaseService.DatabaseServiceBinder databaseServiceBinder =
                        (DatabaseService.DatabaseServiceBinder) service;
                DatabaseService _service = DatabaseRepository.this.service
                        = databaseServiceBinder.getService();
                _service.openDatabase(DatabaseRepository.this.dbName);
                DatabaseRepository.this.isOpened = true;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            DatabaseRepository.this.isOpened = false;
        }
    }
}
