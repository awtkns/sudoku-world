package com.sigma.sudokuworld.persistence;

import android.app.Application;
import android.support.annotation.NonNull;

import com.sigma.sudokuworld.persistence.db.AppDatabase;
import com.sigma.sudokuworld.persistence.db.daos.WordSetDao;
import com.sigma.sudokuworld.persistence.db.entities.Word;

public class WordSetRepository {
    private WordSetDao wordSetDao;

    public WordSetRepository(@NonNull Application application) {
        wordSetDao = AppDatabase.Companion.getInstance(application).getWordSetDao();
    }

    public Word[] getNativeWordsInSet(int setID) {
        return (Word[]) wordSetDao.getAllNativeWordsInSet(setID).toArray();
    }

    public Word[] getForeignWordsInSet(int setID) {
        return (Word[]) wordSetDao.getAllForeignWordsInSet(setID).toArray();
    }
}