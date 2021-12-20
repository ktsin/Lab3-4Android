package com.example.lab3.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import com.example.lab3.R;
import com.example.lab3.activities.SearchResultActivity;

import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;


public class SearchDialog extends DialogFragment {
    public static final String TAG = "SearchDialogTag";
    private final AppCompatActivity activity;
    private String destination = "";
    private OffsetTime time = OffsetTime.MIN;
    private boolean enableDepartureTime = false;
    private SwitchCompat switchTime;
    private EditText destinationEdit;
    private EditText timeEdit;

    public SearchDialog(AppCompatActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_search_dialog, null);
        switchTime = ((SwitchCompat) view.findViewById(R.id.switch_search));
        switchTime.setOnClickListener((v -> enableDepartureTime = !enableDepartureTime));
        destinationEdit = ((EditText) view.findViewById(R.id.destination_search));
        destinationEdit
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        destination = s.toString();
                    }
                });
        timeEdit = ((EditText) view.findViewById(R.id.time_search));
        timeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    time = OffsetTime.parse(s.toString() + "+03:00", DateTimeFormatter.ISO_TIME);
                    timeEdit.setBackgroundColor(getResources().getColor(R.color.white));
                } catch (DateTimeParseException ex) {
                    timeEdit.setBackgroundColor(getResources().getColor(R.color.design_default_color_error));
                }
            }
        });
        builder.setView(view)
                .setTitle("Search record...")
                .setPositiveButton("Search", (dialog, id) -> {
                    Intent intent = prepareLaunchSearchResultActivity();
                    activity.startActivity(intent);
                })
                .setNegativeButton("Cancel",
                        (dialog, id) -> Objects.requireNonNull(SearchDialog.this.getDialog())
                                .cancel());
        return builder.create();

    }

    private Intent prepareLaunchSearchResultActivity(){
        Intent intent = new Intent(activity, SearchResultActivity.class);
        intent.putExtra(SearchResultActivity.DESTINATION_TAG, destination);
        intent.putExtra(SearchResultActivity.DEPARTURE_TIME_TAG, time);
        intent.putExtra(SearchResultActivity.SWITCH_TAG, enableDepartureTime);
        return intent;
    }
}