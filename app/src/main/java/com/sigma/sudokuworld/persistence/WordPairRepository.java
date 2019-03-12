package com.sigma.sudokuworld.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import com.sigma.sudokuworld.persistence.db.AppDatabase;
import com.sigma.sudokuworld.persistence.db.daos.PairDao;
import com.sigma.sudokuworld.persistence.db.daos.PairWithSetDao;
import com.sigma.sudokuworld.persistence.db.entities.Word;
import com.sigma.sudokuworld.persistence.db.entities.Pair;

import android.support.annotation.NonNull;
import com.sigma.sudokuworld.persistence.db.views.WordPair;

import java.util.List;

public class WordPairRepository {
    private PairDao mPairDao;
    private PairWithSetDao mPairWithSetDao;
    private WordRepository mWordRepository;

    private LiveData<List<WordPair>> mAllWordPairs;

    public WordPairRepository(@NonNull Application application) {
        mWordRepository = new WordRepository(application);

        mPairDao = AppDatabase.Companion.getInstance(application).getPairDao();
        mPairWithSetDao = AppDatabase.Companion.getInstance(application).getPairWithSetDao();
        mAllWordPairs = mPairDao.getAllWordPairs();
    }

    public WordPair getWordPair(long pairID) {
        return mPairDao.getWordPair(pairID);
    }

    public LiveData<List<WordPair>> getAllWordPairs() {
        return mAllWordPairs;
    }

    public boolean deletePair(long pairId) {
        if (mPairWithSetDao.getAllSetsForPair(pairId).isEmpty()) {
            mPairDao.delete(pairId);
            return true;

        } else return false;
    }

    public long saveWordPair(Word nativeWord, Word foreignWord) {       //TODO: language code
        long nID = mWordRepository.saveWord(nativeWord);
        long fID = mWordRepository.saveWord(foreignWord);

        Pair pair = mPairDao.getPair(nID, fID);

        long pID;
        if (pair == null) {
            pID = mPairDao.insert(new Pair(0, nID, fID));
        } else {
            pID = pair.getPairID();
        }

        return pID;
    }
}

