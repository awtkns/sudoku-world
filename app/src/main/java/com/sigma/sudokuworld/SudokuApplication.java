package com.sigma.sudokuworld;

import android.app.Application;

import com.sigma.sudokuworld.db.AppDatabase;
import com.sigma.sudokuworld.db.utils.DatabaseInitializer;

public class SudokuApplication extends Application {

    private AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();

        db = AppDatabase.Companion.getInstance(getApplicationContext());
        DatabaseInitializer.Companion.deleteAll(db);
        DatabaseInitializer.Companion.populateDatabase(db);
    }

    public AppDatabase getDB(){
        return db;
    }
}
