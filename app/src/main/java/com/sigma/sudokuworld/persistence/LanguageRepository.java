package com.sigma.sudokuworld.persistence;

import android.app.Application;
import android.support.annotation.NonNull;
import com.sigma.sudokuworld.persistence.db.AppDatabase;
import com.sigma.sudokuworld.persistence.db.daos.LanguageDao;
import com.sigma.sudokuworld.persistence.db.entities.Language;

public class LanguageRepository {
    private LanguageDao mLanguageDao;

    public LanguageRepository(@NonNull Application application) {
        mLanguageDao = AppDatabase.Companion.getInstance(application).getLanguageDao();
    }

    public long insertLanguage(String name, String code) {
        Language language = mLanguageDao.getLanguageByCode(code);

        long langID;
        if (language == null) {
             langID = mLanguageDao.insert(new Language(0, name, code));
        } else {
            langID = language.getLanguageID();
        }

        return langID;
    }
}
