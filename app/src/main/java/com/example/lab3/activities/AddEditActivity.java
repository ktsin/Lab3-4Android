package com.example.lab3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab3.R;
import com.example.lab3.model.Seat;
import com.example.lab3.model.SeatType;
import com.example.lab3.model.Train;
import com.example.lab3.presenter.AddEditActivityPresenter;
import com.example.lab3.presenter.MainActivityPresenter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class AddEditActivity extends AppCompatActivity {
    public final static String ACTIVITY_MODE_FLAG_TAG = "ACTIVITY_EDIT_MODE_FLAG";
    public final static String ACTIVITY_TARGET_TAG = "ACTIVITY_EDIT_TARGET";
    private final String[] DESTINATIONS = new String[]{"Minsk", "Barysaŭ", "Salihorsk", "Maladziečna", "Žodzina", "Homieĺ", "Mazyr", "Žlobin", "Svietlahorsk", "Rečyca", "Kalinkavičy", "Dobruš"};
    private AddEditActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        Intent intent = getIntent();
        boolean mode = intent.getBooleanExtra(ACTIVITY_MODE_FLAG_TAG, false);
        ((AutoCompleteTextView) findViewById(R.id.destination_edit))
                .setAdapter(
                        new ArrayAdapter<String>(this,
                                R.layout.support_simple_spinner_dropdown_item, DESTINATIONS));
        if (!mode) {
            ArrayList<Seat> seats = new ArrayList<>();
            seats.add(new Seat(SeatType.SHARED, 0));
            seats.add(new Seat(SeatType.RESERVED_SEAT, 0));
            seats.add(new Seat(SeatType.COMPARTMENT, 0));
            seats.add(new Seat(SeatType.LUXURY, 0));
            presenter = new AddEditActivityPresenter(new Train(null, null, null, seats), mode);
        } else {
            Train target = (Train) intent.getSerializableExtra(ACTIVITY_TARGET_TAG);
            presenter = new AddEditActivityPresenter(target, mode);

            ((EditText) findViewById(R.id.train_number_edit)).setText(target.getTrainNumber());
            ((EditText) findViewById(R.id.destination_edit)).setText(target.getDestination());


            ((EditText) findViewById(R.id.departure_time_edit)).setText(target.getDepartureTime()
                    .format(DateTimeFormatter.ofPattern("HH:mm")));
            ArrayList<Seat> seats = target.getSeats();
            if (seats != null) {
                Optional<Seat> seat = seats
                        .stream()
                        .filter(s -> s.getSeatType() == SeatType.RESERVED_SEAT)
                        .findFirst();
                seat.ifPresent(value -> ((EditText) findViewById(R.id.shared_seats_edit)).setText(String.valueOf(value.getCount())));

                seat = seats
                        .stream()
                        .filter(s -> s.getSeatType() == SeatType.COMPARTMENT)
                        .findFirst();

                seat.ifPresent(value -> ((EditText) findViewById(R.id.compartment_seats_edit)).setText(String.valueOf(value.getCount())));

                seat = seats
                        .stream()
                        .filter(s -> s.getSeatType() == SeatType.RESERVED_SEAT)
                        .findFirst();

                seat.ifPresent(value -> ((EditText) findViewById(R.id.reserved_seats_edit)).setText(String.valueOf(value.getCount())));

                seat = seats
                        .stream()
                        .filter(s -> s.getSeatType() == SeatType.LUXURY)
                        .findFirst();

                seat.ifPresent(value -> ((EditText) findViewById(R.id.luxury_seats_edit)).setText(String.valueOf(value.getCount())));
            }
        }

        ((EditText) findViewById(R.id.train_number_edit))
                .addTextChangedListener(new EditTextWatcher(R.id.train_number_edit, presenter));
        ((EditText) findViewById(R.id.destination_edit))
                .addTextChangedListener(new EditTextWatcher(R.id.destination_edit, presenter));
        ((EditText) findViewById(R.id.departure_time_edit))
                .addTextChangedListener(new EditTextWatcher(R.id.departure_time_edit, presenter));
        ((EditText) findViewById(R.id.shared_seats_edit))
                .addTextChangedListener(new EditTextWatcher(R.id.shared_seats_edit, presenter));
        ((EditText) findViewById(R.id.compartment_seats_edit))
                .addTextChangedListener(new EditTextWatcher(R.id.compartment_seats_edit, presenter));
        ((EditText) findViewById(R.id.reserved_seats_edit))
                .addTextChangedListener(new EditTextWatcher(R.id.reserved_seats_edit, presenter));
        ((EditText) findViewById(R.id.luxury_seats_edit))
                .addTextChangedListener(new EditTextWatcher(R.id.luxury_seats_edit, presenter));

        ((Button) findViewById(R.id.edit_save_button)).setOnClickListener(v -> {
            presenter.onSaveButtonClick();
            //mainActivityPresenter.redrawFragments();
            setResult(RESULT_OK);
            finish();
        });
        ((Button) findViewById(R.id.cancel_edit_button)).setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Add/Edit entry");
        } catch (NullPointerException e) {
            Log.e(e.getClass().getName(), e.getMessage());
        }
    }


    private class EditTextWatcher implements TextWatcher {
        private final int textId;
        private final AddEditActivityPresenter presenter;

        public EditTextWatcher(int textId, AddEditActivityPresenter presenter) {
            this.textId = textId;
            this.presenter = presenter;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (textId != R.id.departure_time_edit) {
                String field = s.toString();
                presenter.updateModelField(textId, field);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            String field = s.toString();
            presenter.updateModelField(textId, field);
        }
    }
}