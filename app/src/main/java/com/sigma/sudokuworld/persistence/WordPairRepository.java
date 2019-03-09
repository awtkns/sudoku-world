package com.sigma.sudokuworld.persistence;

import android.app.Application;
;
import com.sigma.sudokuworld.persistence.db.AppDatabase;
import com.sigma.sudokuworld.persistence.db.daos.LanguageDao;
import com.sigma.sudokuworld.persistence.db.daos.WordDao;
import com.sigma.sudokuworld.persistence.db.daos.WordPairDao;
import com.sigma.sudokuworld.persistence.db.entities.Word;
import com.sigma.sudokuworld.persistence.db.entities.WordPair;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class WordPairRepository {
    private WordPairDao wordPairDao;
    private WordDao wordDao;
    private LanguageDao languageDao;

    public WordPairRepository(@NonNull Application application) {
        wordPairDao = AppDatabase.Companion.getInstance(application).getWordPairDao();
        wordDao = AppDatabase.Companion.getInstance(application).getWordDao();
        languageDao = AppDatabase.Companion.getInstance(application).getLanguageDao();
    }

    public WordPairInformative getWordPairInformative(int pairID) {
        WordPair pair = wordPairDao.getWordPairByID(pairID);
        String nWord = wordDao.getWordByID(pair.getNativeWordID()).getWord();
        String fWord = wordDao.getWordByID(pair.getForeignWordID()).getWord();
        return new WordPairInformative(pair, nWord, fWord);
    }

    public List<WordPairInformative> getAllWordPairsInformative() {
        List<WordPair> pairs = wordPairDao.getAll();

        List<WordPairInformative> wordPairsInfo = new ArrayList<>();
        for (WordPair pair : pairs) {
                String nWord = wordDao.getWordByID(pair.getNativeWordID()).getWord();
                String fWord = wordDao.getWordByID(pair.getForeignWordID()).getWord();
                wordPairsInfo.add(new WordPairInformative(pair, nWord, fWord));
        }

        return wordPairsInfo;
    }

    public void saveWordPair(String nativeWord, String foreignWord) {
        Word nWord = new Word(0, 1, nativeWord);    //TODO: save lang
        Word fWord = new Word(0, 1, foreignWord);

        int nID = (int) wordDao.insert(nWord);
        int fID = (int) wordDao.insert(fWord);
        wordPairDao.insert(new WordPair(0, nID, fID));
    }

    public class WordPairInformative {
        private final WordPair wordPair;
        private final String foreignWordString;
        private final String nativeWordString;

        public WordPairInformative(WordPair wordPair, String nativeWordString, String foreignWordString) {
            this.wordPair = wordPair;
            this.nativeWordString = nativeWordString;
            this.foreignWordString = foreignWordString;
        }

        public WordPair getWordPair() {
            return wordPair;
        }

        public String getForeignWordString() {
            return foreignWordString;
        }

        public String getNativeWordString() {
            return nativeWordString;
        }
    }
}

