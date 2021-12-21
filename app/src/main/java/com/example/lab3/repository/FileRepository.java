package com.example.lab3.repository;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.lab3.model.Train;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileRepository implements IRepository {
    private final Context context;
    private ArrayList<Train> cache = new ArrayList<Train>();
    private Uri fileUri;
    private boolean isOpened = false;
    private Reader reader;
    private FileWriter writer;
    private File file;

    public FileRepository(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public boolean open(String uri) {
        boolean openResult = false;
        try {
//            File file = new File(uri);
//            fileUri = Uri.fromFile(file);
//            reader = new BufferedReader(new FileReader(file));
//            fileUri = uri;
            fileUri = Uri.parse(uri);
            Log.d("D001", String.valueOf(uri));
            InputStream input = context.getContentResolver().openInputStream(fileUri);
            reader = new BufferedReader(new InputStreamReader(input));
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<Train>>() {
            }.getType();
            ArrayList<Train> trains = gson.fromJson(reader, collectionType);
            if (trains != null) {
                cache = trains;
            }
            Log.d("D001", String.valueOf(trains));
            openResult = true;
        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.getMessage());
        }
        return isOpened = openResult;
    }

    @Override
    public boolean close() {
        isOpened = false;
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                Log.e(e.getClass().getName(), e.getMessage());
            }
        }
        cache.clear();
        return isOpened;
    }

    @Override
    public boolean save() {
        boolean result = false;
        try {
            Log.d("D002", "save repository");
//            writer = new FileWriter(file, false);
            OutputStream output = context.getContentResolver().openOutputStream(fileUri);
            OutputStreamWriter writer = new OutputStreamWriter(output);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String str = gson.toJson(cache);
            writer.write(str);
            writer.flush();
            writer.close();
            output.close();
            result = true;
        } catch (Exception exception) {
            Log.e(exception.getClass().getName(), exception.getMessage());
        }
        return result;
    }

    @Override
    public void refresh() {

    }

    @Override
    public ArrayList<Train> getAll() {
        if (isOpened)
            return cache;
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Train> search(String destination) {
        return (ArrayList<Train>) cache.stream().filter(e -> e.getDestination().contains(destination)).collect(Collectors.toList());
    }

    @Override
    public ArrayList<Train> search(String destination, OffsetTime departureTime) {
        return (ArrayList<Train>) cache.stream().filter(e -> e.getDestination().contains(destination) && e.getDepartureTime().compareTo(departureTime)>=0).collect(Collectors.toList());
    }

    @Override
    public boolean remove(Train train) {
        return cache.remove(train);
    }

    @Override
    public boolean add(Train train) {
        return cache.add(train);
    }

    @Override
    public boolean update(Train old, Train newTrain) {
        cache.remove(old);
        return cache.add(newTrain);
    }

    public boolean isOpened() {
        return isOpened;
    }

    @Override
    public Train getById(int id) {
        Optional<Train> train = cache.stream().filter(e -> e.getId() == id).findFirst();
        return train.orElse(null);
    }
}
