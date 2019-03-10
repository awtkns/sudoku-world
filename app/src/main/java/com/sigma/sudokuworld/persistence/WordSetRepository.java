package com.sigma.sudokuworld.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.sigma.sudokuworld.persistence.db.AppDatabase;
import com.sigma.sudokuworld.persistence.db.daos.SetDao;
import com.sigma.sudokuworld.persistence.db.daos.PairWithSetDao;
import com.sigma.sudokuworld.persistence.db.entities.Set;
import com.sigma.sudokuworld.persistence.db.entities.Word;
import com.sigma.sudokuworld.persistence.db.entities.Pair;
import com.sigma.sudokuworld.persistence.db.entities.PairWithSet;
import com.sigma.sudokuworld.persistence.db.views.WordPair;

import java.util.List;

public class WordSetRepository {
    private PairWithSetDao mPairWithSetDao;
    private SetDao setDao;
    private LiveData<List<Set>> mAllSets;

    public WordSetRepository(@NonNull Application application) {
        AppDatabase database = AppDatabase.Companion.getInstance(application);
        mPairWithSetDao = database.getWordSetDao();
        setDao = database.getSetDao();
        mAllSets = setDao.getAllLiveData();
    }

    public LiveData<List<Set>> getAllSets() {
        return mAllSets;
    }

    public Set getSet(int setId) {
        return setDao.getSetByID(setId);
    }


    public void deleteSet(Set set) {
        setDao.delete(set);
    }

    public void saveSet(String name, String description, List<WordPair> wordPairs) {
        int setId = (int) setDao.insert(new Set(0, name, description));

        for (WordPair wp : wordPairs) {
            mPairWithSetDao.insert(new PairWithSet(setId, wp.getForeignWord().getLanguageID())); //TODO fix
        }

    }

    public Word[] getNativeWordsInSet(int setID) {
        return (Word[]) mPairWithSetDao.getAllNativeWordsInSet(setID).toArray();
    }

    public Word[] getForeignWordsInSet(int setID) {
        return (Word[]) mPairWithSetDao.getAllForeignWordsInSet(setID).toArray();
    }
}