package com.sigma.sudokuworld.persistence;

import android.app.Application;
import android.support.annotation.NonNull;
import com.sigma.sudokuworld.persistence.db.AppDatabase;
import com.sigma.sudokuworld.persistence.db.daos.WordDao;
import com.sigma.sudokuworld.persistence.db.entities.Word;

public class WordRepository {
    private WordDao mWordDao;

    public WordRepository(@NonNull Application application) {
        mWordDao = AppDatabase.Companion.getInstance(application).getWordDao();
    }

    public long saveWord(Word word) {

        //Making Sure its not already in the db
        Word w = mWordDao.getWord(word.getWord(), word.getLanguageID());

        long wID;
        if (w == null) {
            //Not in the db
            word.setWordID(0);
            wID = mWordDao.insert(word);
        } else {
            wID = w.getWordID();
        }

        return wID;
    }
}
