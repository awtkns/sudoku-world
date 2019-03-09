package com.sigma.sudokuworld.persistence;

import android.app.Application;
import android.support.annotation.NonNull;

import com.sigma.sudokuworld.persistence.db.AppDatabase;
import com.sigma.sudokuworld.persistence.db.daos.SetDao;
import com.sigma.sudokuworld.persistence.db.daos.WordSetDao;
import com.sigma.sudokuworld.persistence.db.entities.Set;
import com.sigma.sudokuworld.persistence.db.entities.Word;
import com.sigma.sudokuworld.persistence.db.entities.WordPair;
import com.sigma.sudokuworld.persistence.db.entities.WordSet;

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


    public void deleteSet(Set set) {
        setDao.delete(set);
    }

    public void newWordSet(String name, String description, List<WordPair> wordPairs) {
        int setId = (int) setDao.insert(new Set(0, name, description));

        for (WordPair wp : wordPairs) {
            wordSetDao.insert(new WordSet(setId, wp.getWordPairID()));
        }

    }

    public Word[] getNativeWordsInSet(int setID) {
        return (Word[]) wordSetDao.getAllNativeWordsInSet(setID).toArray();
    }

    public Word[] getForeignWordsInSet(int setID) {
        return (Word[]) wordSetDao.getAllForeignWordsInSet(setID).toArray();
    }
}