package com.sigma.sudokuworld.persistence;

import android.app.Application;
;
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
    private WordDao wordDao;
    private LanguageDao languageDao;

    public WordPairRepository(@NonNull Application application) {
        mPairDao = AppDatabase.Companion.getInstance(application).getWordPairDao();
        wordDao = AppDatabase.Companion.getInstance(application).getWordDao();
        languageDao = AppDatabase.Companion.getInstance(application).getLanguageDao();
    }

    public WordPair getWordPair(int pairID) {
        return mPairDao.getWordPair(pairID);
    }

    public List<WordPair> getAllWordPairs() {
        return mPairDao.getAllWordPairs();
    }

    public void saveWordPair(String nativeWord, String foreignWord) {
        Word nWord = new Word(0, 1, nativeWord);    //TODO: save lang
        Word fWord = new Word(0, 1, foreignWord);

        int nID = (int) wordDao.insert(nWord);
        int fID = (int) wordDao.insert(fWord);
        mPairDao.insert(new Pair(0, nID, fID));
    }
}

