package com.example.lab3.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.lab3.R;
import com.example.lab3.model.Seat;
import com.example.lab3.model.SeatType;
import com.example.lab3.model.Train;
import com.example.lab3.repository.IRepository;
import com.example.lab3.repository.RepositoryHolder;

import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

public class AddEditActivityPresenter {
    private final Train target;
    private final Train oldValues;
    private IRepository repository;
    private final boolean isEdit;

    public AddEditActivityPresenter(Train target, boolean isEdit) {
        this.repository = RepositoryHolder.getMainRepository();
        this.target = target;
        this.oldValues = target;
        this.isEdit = isEdit;
    }

    public boolean addTrain(@NonNull Train train) {
        return repository.add(train);
    }

    public boolean replaceTrain(@NonNull Train oldValue, @NonNull Train newValue) {
        return repository.update(oldValue, newValue);
    }

    public Train getTarget(int id) {
        if (repository == null) {
            repository = RepositoryHolder.getMainRepository();
        }
        return repository.getById(id);
    }

    public void updateModelField(int id, String value) {
        if (id == R.id.train_number_edit) {
            target.setTrainNumber(value);
        } else if (id == R.id.destination_edit) {
            target.setDestination(value);
        } else if (id == R.id.departure_time_edit) {
            try {
                OffsetTime time = OffsetTime.parse(value+"+03:00", DateTimeFormatter.ISO_TIME);
                target.setDepartureTime(time);
            } catch (java.time.format.DateTimeParseException exception) {
                Log.d("PARSE ERR",
                        String.format("Value: %s, exception: %s", value, exception));
            }

        } else
            try {
                int count = Integer.parseInt(value);
                if (id == R.id.shared_seats_edit) {
                    Optional<Seat> seat =
                            target.getSeats()
                                    .stream()
                                    .filter(s -> s.getSeatType() == SeatType.SHARED)
                                    .findFirst();
                    seat.ifPresent(s -> s.setCount(count));
                } else if (id == R.id.compartment_seats_edit) {
                    Optional<Seat> seat =
                            target.getSeats()
                                    .stream()
                                    .filter(s -> s.getSeatType() == SeatType.COMPARTMENT)
                                    .findFirst();
                    seat.ifPresent(s -> s.setCount(count));
                } else if (id == R.id.reserved_seats_edit) {
                    Optional<Seat> seat =
                            target.getSeats()
                                    .stream()
                                    .filter(s -> s.getSeatType() == SeatType.RESERVED_SEAT)
                                    .findFirst();
                    seat.ifPresent(s -> s.setCount(count));
                } else if (id == R.id.luxury_seats_edit) {
                    Optional<Seat> seat =
                            target.getSeats()
                                    .stream()
                                    .filter(s -> s.getSeatType() == SeatType.LUXURY)
                                    .findFirst();
                    seat.ifPresent(s -> s.setCount(count));
                }
            } catch (NumberFormatException exception) {
                Log.d("PARSE ERR",
                        String.format("Value: %s, exception: %s", value, exception));
            }
    }

    public void onSaveButtonClick(){
        if (!isEdit){
            target.setId(new Random().nextInt());
            repository.add(target);
        }
        else
            repository.update(oldValues, target);
    }

}
