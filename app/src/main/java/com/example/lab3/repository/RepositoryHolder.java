package com.example.lab3.repository;

import android.content.Context;

public class RepositoryHolder {
    private static IRepository mainRepository;
    private static Context applicationContext;


    public static IRepository getMainRepository() {
        return mainRepository;
    }

    public static void setMainRepository(IRepository mainRepository) {
        RepositoryHolder.mainRepository = mainRepository;
    }

    public static Context getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(Context context) {
        RepositoryHolder.applicationContext = context;
    }
}
