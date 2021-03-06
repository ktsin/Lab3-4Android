package com.example.lab3.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab3.R;
import com.example.lab3.activities.adapters.MainListAdapter;
import com.example.lab3.model.Train;
import com.example.lab3.presenter.SearchPresenter;

import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SearchResultActivity extends AppCompatActivity {
    public static String DESTINATION_TAG = "SearchResultActivity.destination";
    public static String DEPARTURE_TIME_TAG = "SearchResultActivity.departureTime";
    public static String SWITCH_TAG = "SearchResultActivity.switchMode";
    private SearchPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        presenter = new SearchPresenter();
        if (getActionBar() != null)
            getActionBar().setTitle("Search result");
        Intent intent = getIntent();
        String dest = intent.getStringExtra(DESTINATION_TAG);
        OffsetTime time = (OffsetTime) intent.getSerializableExtra(DEPARTURE_TIME_TAG);
        boolean mode = intent.getBooleanExtra(SWITCH_TAG, false);
        List<Train> trains = presenter.executeSearchQuery(dest, time, mode);
        MainListAdapter adapter = new MainListAdapter(this, new ArrayList<Train>(trains));
        ((RecyclerView)findViewById(R.id.recyclerList)).setAdapter(adapter);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Search result: " + String.valueOf(trains.size()));
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
//        adapter.addAll(trains.stream().map(Train::toString).collect(Collectors.toList()));
//        setListAdapter(adapter);
    }
}
