package com.example.lab3.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.lab3.R;
import com.example.lab3.activities.RecyclerViewContextMenuInfo;
import com.example.lab3.activities.adapters.MainListAdapter;
import com.example.lab3.model.Train;
import com.example.lab3.presenter.MainActivityPresenter;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MainActivityPresenter presenter;
    private OnOpenFileClickListener onOpenFileClickListener;
    private MainListAdapter adapter;
    private RecyclerView recyclerView;



    public MainListFragment() {
        // Required empty public constructor
    }

    public static MainListFragment newInstance(String param1, String param2) {
        MainListFragment fragment = new MainListFragment();
        Bundle args = new Bundle();

        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onResume() {
        Log.d("D0012", "MainListFragment.onResume()");
        super.onResume();
        //requestNewDataFromPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_main_list, container, false);
        ((Button) view.findViewById(R.id.open_file_button)).setOnClickListener((view_) -> {
            Toast.makeText(this.getContext(), "Open file click", Toast.LENGTH_LONG).show();
            if(onOpenFileClickListener != null){
                onOpenFileClickListener.onOpenFileClickListener();
            }
        });
        presenter.setOnSuccessfullyFileOpenedListener(this::requestNewDataFromPresenter);
        recyclerView = ((RecyclerView)view.findViewById(R.id.main_list_rw));
        registerForContextMenu(recyclerView);
        adapter = presenter.getMainListAdapter();
        Log.d("D00009", String.valueOf(adapter));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu,
                                    @NonNull View v,
                                    @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        RecyclerViewContextMenuInfo recyclerViewContextMenuInfo = new RecyclerViewContextMenuInfo(v);
        super.onCreateContextMenu(menu, v, recyclerViewContextMenuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.main_contextual_menu, menu);
    }

    public void setPresenter(MainActivityPresenter presenter){
        this.presenter = presenter;
    }

    public void setOnOpenFileClickListener(OnOpenFileClickListener listener){
        this.onOpenFileClickListener = listener;
    }

    private void requestNewDataFromPresenter(){
        ArrayList<Train> trains = presenter.getList();
        if(adapter == null){
            adapter = new MainListAdapter(this.getContext(), trains);
            presenter.setMainListAdapter(adapter);
            recyclerView.setAdapter(adapter);
        }
        else{
            adapter.dropData();
            adapter.addData(trains);
            adapter.notifyDataSetChanged();
            //recyclerView.setAdapter(adapter);
        }
        presenter.redrawFragments();
    }


    public interface OnOpenFileClickListener{
        void onOpenFileClickListener();
    }


}