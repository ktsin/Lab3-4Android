package com.example.lab3.repository;

public class RepositoryHolder {
    private static IRepository mainRepository;


    public static IRepository getMainRepository() {
        return mainRepository;
    }

    public static void setMainRepository(IRepository mainRepository) {
        RepositoryHolder.mainRepository = mainRepository;
    }
}
