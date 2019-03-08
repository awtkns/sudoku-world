package com.sigma.sudokuworld.persistence.db.utils

import com.sigma.sudokuworld.persistence.db.AppDatabase
import com.sigma.sudokuworld.persistence.db.entities.Language
import com.sigma.sudokuworld.persistence.db.entities.Set

abstract class DatabaseInitializer {

    //Kotlin equivalent to static
    companion object {

        fun populateDatabase(db: AppDatabase) {
//            addLanguage(db, "English", "en")
//            addLanguage(db, "French", "fr")
//            addLanguage(db, "Spanish", "es")
//            addLanguage(db, "Russian", "run")
            addSet(db,"Default Set", "The default set")
        }

        //Caution
        fun deleteAll(db: AppDatabase) {
            db.getLanguageDao().deleteAll()
        }

        private fun addLanguage(db: AppDatabase, languageName: String, code: String) {
            val lang = Language(0, languageName, code)

            db.getLanguageDao().insert(lang)
        }

        private fun addSet(db: AppDatabase, setName: String, description: String) {
            val set = Set(1, setName, description)

            db.getSetDao().insert(set)
        }
    }
}