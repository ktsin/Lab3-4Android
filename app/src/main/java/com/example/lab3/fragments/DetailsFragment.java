package com.example.lab3.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lab3.R;
import com.example.lab3.model.Seat;
import com.example.lab3.model.Train;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {
    private static final String ARG_PARAM1 = "TRAIN_INFORMATION";

    // TODO: Rename and change types of parameters
    private Train train;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param train Train information.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(Train train) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, train);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            train = (Train) getArguments().getSerializable(ARG_PARAM1);

        }
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        if(train != null){
            ((TextView)view.findViewById(R.id.train_number_details))
                    .setText(train.getTrainNumber());
            ((TextView)view.findViewById(R.id.destination_details))
                    .setText(train.getDestination());
            ((TextView)view.findViewById(R.id.departure_time_details))
                    .setText(train.getDepartureTime()
                            .format(DateTimeFormatter.ISO_OFFSET_TIME));
            ArrayList<Seat> seats = train.getSeats();
            seats.forEach(e -> {
                TextView text = null;
                switch (e.getSeatType()){
                    case SHARED:
                        text = (TextView)view.findViewById(R.id.shared_seats_details);
                        break;
                    case COMPARTMENT:
                        text = (TextView)view.findViewById(R.id.compartment_seats_details);
                        break;
                    case RESERVED_SEAT:
                        text = (TextView)view.findViewById(R.id.reserved_seats_details);
                        break;
                    case LUXURY:
                        text = (TextView)view.findViewById(R.id.luxury_seats_details);
                        break;
                }
                if(text != null)
                    text.setText(String.valueOf(e.getCount()));
            });
        }
        return view;
    }
}