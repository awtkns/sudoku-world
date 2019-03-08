package com.sigma.sudokuworld.persistence;

import android.app.Application;
import android.support.annotation.NonNull;

import com.sigma.sudokuworld.persistence.db.AppDatabase;
import com.sigma.sudokuworld.persistence.db.daos.SetDao;
import com.sigma.sudokuworld.persistence.db.daos.WordSetDao;
import com.sigma.sudokuworld.persistence.db.entities.Set;
import com.sigma.sudokuworld.persistence.db.entities.Word;

import java.util.List;

public class WordSetRepository {
    private WordSetDao wordSetDao;
    private SetDao setDao;

    public WordSetRepository(@NonNull Application application) {
        AppDatabase database = AppDatabase.Companion.getInstance(application);
        wordSetDao = database.getWordSetDao();
        setDao = database.getSetDao();
    }

    public List<Set> getAllSets() {
        return setDao.getAll();
    }

    public Set getSet(int setId) {
        return setDao.getSetByID(setId);
    }


    public Word[] getNativeWordsInSet(int setID) {
        return (Word[]) wordSetDao.getAllNativeWordsInSet(setID).toArray();
    }

    public Word[] getForeignWordsInSet(int setID) {
        return (Word[]) wordSetDao.getAllForeignWordsInSet(setID).toArray();
    }
}