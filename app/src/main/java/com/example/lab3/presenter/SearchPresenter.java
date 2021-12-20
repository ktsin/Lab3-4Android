package com.example.lab3.presenter;

import com.example.lab3.model.Train;
import com.example.lab3.repository.IRepository;
import com.example.lab3.repository.RepositoryHolder;

import java.time.OffsetTime;
import java.util.List;

public class SearchPresenter {
    private final IRepository repository;

    public SearchPresenter(){
        repository = RepositoryHolder.getMainRepository();
    }

    public List<Train> executeSearchQuery(String destination, OffsetTime time, boolean mode){
        if(!mode)
            return repository.search(destination);
        else
            return repository.search(destination, time);
    }
}
