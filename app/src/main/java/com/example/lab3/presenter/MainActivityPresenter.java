package com.example.lab3.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.lab3.activities.adapters.MainActivityPageAdapter;
import com.example.lab3.activities.adapters.MainListAdapter;
import com.example.lab3.model.Train;
import com.example.lab3.repository.IRepository;

import java.util.ArrayList;
import java.util.Optional;

public class MainActivityPresenter {
    private final IRepository repository;
    private Uri connectionUri;
    private OnOpenFileClickListener onOpenFileClickListener;
    private OnSuccessfullyFileOpenedListener onSuccessfullyFileOpenedListener;
    private MainActivityPageAdapter pagerAdapter;
    private MainListAdapter mainListAdapter;

    public MainActivityPresenter(IRepository repository){
        this.repository = repository;
    }

    public ArrayList<Train> getList(){
        return requestDataFromRepository();
    }

    public Train getById(int id){
        Optional<Train> train =  requestDataFromRepository()
                .stream()
                .filter(e -> e.getId() == id)
                .findFirst();
        return train.orElse(null);
    }

    public void setConnectionUri(Uri uri, Context context){
        connectionUri = uri;
        if(onSuccessfullyFileOpenedListener != null){
            onSuccessfullyFileOpenedListener.onSuccessfullyFileOpenedListener();
        }
    }

    public void setOnOpenFileClickListener(OnOpenFileClickListener listener){
        onOpenFileClickListener = listener;
    }

    public void setOnSuccessfullyFileOpenedListener(OnSuccessfullyFileOpenedListener listener){
        onSuccessfullyFileOpenedListener = listener;
    }

    public void notifyDataChanged(){
        if(onSuccessfullyFileOpenedListener != null){
            onSuccessfullyFileOpenedListener.onSuccessfullyFileOpenedListener();
        }
    }

    public void onSaveMenuItemClick(){
        repository.save();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void redrawFragments(){
        Log.d("D004", "redrawFragments()");
        Log.d("D004", String.valueOf(pagerAdapter));
        Log.d("D004", String.valueOf(mainListAdapter));


        if(pagerAdapter != null)
            pagerAdapter.notifyItemRangeChanged(0, 1);
        if(mainListAdapter != null)
            mainListAdapter.notifyDataSetChanged();

    }

    public ArrayList<Train> requestDataFromRepository(){
        if (!repository.isOpened()) {
            repository.open(connectionUri);
        }
        return repository.getAll();
    }

    public void requestForDelete(int elementId){
        Train train = repository.getById(elementId);
        repository.remove(train);
        notifyDataChanged();
    }

    public void setPagerAdapter(MainActivityPageAdapter pagerAdapter) {
        this.pagerAdapter = pagerAdapter;
    }

    public MainListAdapter getMainListAdapter() {
        return mainListAdapter;
    }

    public void setMainListAdapter(MainListAdapter mainListAdapter) {
        this.mainListAdapter = mainListAdapter;
    }

    public interface OnOpenFileClickListener{
        void onOpenFileClickListener();
    }

    public interface OnSuccessfullyFileOpenedListener {
        void onSuccessfullyFileOpenedListener();
    }
}
