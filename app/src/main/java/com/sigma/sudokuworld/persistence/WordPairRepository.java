package com.sigma.sudokuworld.persistence;

import android.app.Application;
;
import android.arch.lifecycle.LiveData;
import com.sigma.sudokuworld.persistence.db.AppDatabase;
import com.sigma.sudokuworld.persistence.db.daos.LanguageDao;
import com.sigma.sudokuworld.persistence.db.daos.WordDao;
import com.sigma.sudokuworld.persistence.db.daos.PairDao;
import com.sigma.sudokuworld.persistence.db.entities.Word;
import com.sigma.sudokuworld.persistence.db.entities.Pair;

import android.support.annotation.NonNull;
import com.sigma.sudokuworld.persistence.db.views.WordPair;

import java.util.List;

public class WordPairRepository {
    private PairDao mPairDao;
    private WordDao mWordDao;
    private LanguageDao mLanguageDao;

    private LiveData<List<WordPair>> mAllWordPairs;

    public WordPairRepository(@NonNull Application application) {
        mPairDao = AppDatabase.Companion.getInstance(application).getWordPairDao();
        mWordDao = AppDatabase.Companion.getInstance(application).getWordDao();
        mLanguageDao = AppDatabase.Companion.getInstance(application).getLanguageDao();

        mAllWordPairs = mPairDao.getAllWordPairs();
    }

    public WordPair getWordPair(long pairID) {
        return mPairDao.getWordPair(pairID);
    }

    public LiveData<List<WordPair>> getAllWordPairs() {
        return mAllWordPairs;
    }

    public void saveWordPair(Word nativeWord, Word foreignWord) {
        long nID = mWordDao.insert(nativeWord);
        long fID = mWordDao.insert(foreignWord);
        mPairDao.insert(new Pair(0, nID, fID));
    }
}

