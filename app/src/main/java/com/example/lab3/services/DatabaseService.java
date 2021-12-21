package com.example.lab3.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.widget.ArrayAdapter;

import androidx.room.Room;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.lab3.model.Train;
import com.example.lab3.repository.RepositoryHolder;
import com.example.lab3.repository.databaseHelper.TrainDatabase;

import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class DatabaseService extends Service {
    private final IBinder binder;
    private TrainDatabase db;
    private boolean isOpened;

    public DatabaseService() {
        binder = new DatabaseServiceBinder();
    }

    public void openDatabase(String dbName) {
        AsyncTask<String, Void, Void> task = new AsyncTask<String, Void, Void>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(String... strings) {
                db = Room.databaseBuilder(RepositoryHolder.getApplicationContext(),
                        TrainDatabase.class, strings[0])
                        .fallbackToDestructiveMigration()
                        .build();
                isOpened = true;
                return null;
            }
        };
        task.execute(dbName);
    }

    public void checkpoint() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(Void... voids) {
                if (db != null) {
                    db.trainsDao().save(new SimpleSQLiteQuery("PRAGMA wal_checkpoint(full)"));
                }
                return null;
            }
        };
        task.execute();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public ArrayList<Train> getAll() {
        ArrayList<Train> trains = new ArrayList<>();
        AsyncTask<Void, Void, ArrayList<Train>> task = new AsyncTask<Void, Void, ArrayList<Train>>() {
            @Override
            protected ArrayList<Train> doInBackground(Void... voids) {
                return new ArrayList<Train>(db.trainsDao().getAllTrains());
            }
        };
        try {
            trains = task.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return trains;
    }

    public void remove(Train train) {
        AsyncTask<Train, Void, Void> task = new AsyncTask<Train, Void, Void>() {
            @Override
            protected Void doInBackground(Train... trains) {
                db.trainsDao().remove(trains[0]);
                return null;
            }
        };
        task.execute(train);
    }

    public void add(Train train) {
        AsyncTask<Train, Void, Void> task = new AsyncTask<Train, Void, Void>() {
            @Override
            protected Void doInBackground(Train... trains) {
                db.trainsDao().insert(trains[0]);
                return null;
            }
        };
        task.execute(train);
    }

    public void update(Train newTrain) {
        AsyncTask<Train, Void, Void> task = new AsyncTask<Train, Void, Void>() {
            @Override
            protected Void doInBackground(Train... trains) {
                db.trainsDao().update(trains[0]);
                return null;
            }
        };
        task.execute(newTrain);
    }

    public Train getById(int id) {
        Train train = null;
        AsyncTask<Integer, Void, Train> task = new AsyncTask<Integer, Void, Train>() {
            @Override
            protected Train doInBackground(Integer... integers) {
                return db.trainsDao().getById(integers[0]);
            }
        };
        try {
            train = task.execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return train;
    }

    public ArrayList<Train> search(String departurePoint) {
        ArrayList<Train> trains = new ArrayList<>();
        AsyncTask<String, Void, ArrayList<Train>> task = new AsyncTask<String, Void, ArrayList<Train>>() {
            @Override
            protected ArrayList<Train> doInBackground(String... strings) {
                return new ArrayList<Train>(db.trainsDao().searchByDestination(strings[0]));
            }
        };
        try {
            trains = task.execute(departurePoint).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return trains;
    }

    public ArrayList<Train> search(String destinationPoint, OffsetTime departureTime) {
        ArrayList<Train> trains = search(destinationPoint);
        return trains
                .stream()
                .filter(e -> e.getDepartureTime() != null && e.getDepartureTime().isAfter(departureTime))
                .collect(Collectors.toCollection(ArrayList::new));
    }


    public class DatabaseServiceBinder extends Binder {
        public DatabaseService getService() {
            return DatabaseService.this;
        }
    }
}