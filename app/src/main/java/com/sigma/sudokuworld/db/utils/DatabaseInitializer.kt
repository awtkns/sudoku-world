package com.sigma.sudokuworld.db.utils

import com.sigma.sudokuworld.db.AppDatabase
import com.sigma.sudokuworld.db.Language

class DatabaseInitializer {

    //Kotlin equivalent to static
    companion object {

        fun populateDatabase(db: AppDatabase) {
            addLanguage(db, "English", "en")
            addLanguage(db, "French", "fr")
            addLanguage(db, "Spanish", "es")
            addLanguage(db, "Russian", "run")
        }

        //Caution
        fun deleteAll(db: AppDatabase) {
            db.getLanguageDao().deleteAll()
        }

        private fun addLanguage(db: AppDatabase, languageName: String, code: String) {
            val lang = Language(0, languageName, code)

            db.getLanguageDao().insert(lang)
        }
    }


}