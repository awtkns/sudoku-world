package com.sigma.sudokuworld.persistence.db.utils

import com.sigma.sudokuworld.persistence.db.AppDatabase
import com.sigma.sudokuworld.persistence.db.entities.Language
import com.sigma.sudokuworld.persistence.db.entities.Set
import com.sigma.sudokuworld.persistence.db.entities.Word
import com.sigma.sudokuworld.persistence.db.entities.Pair

abstract class DatabaseInitializer {

    //Kotlin equivalent to static
    companion object {

        fun populateDatabase(db: AppDatabase) {
            addLanguage(db, "English", "en")
//            addLanguage(db, "French", "fr")
//            addLanguage(db, "Spanish", "es")
//            addLanguage(db, "Russian", "run")
            addSet(db,"Default Set", "The default set")
            addWordPair(db, "English", 1, "French", 1)
            addWordPair(db, "English", 1, "French", 1)
        }

        //Caution
        private fun deleteAll(db: AppDatabase) {
            db.getLanguageDao().deleteAll()
        }

        private fun addLanguage(db: AppDatabase, languageName: String, code: String) {
            val lang = Language(1, languageName, code)

            db.getLanguageDao().insert(lang)
        }

        private fun addSet(db: AppDatabase, setName: String, description: String) {
            val set = Set(1, setName, description)

            db.getSetDao().insert(set)
        }

        private fun addWordPair(db: AppDatabase, nativeWord: String, nativeLanguageID: Int, foreignWord: String, foreignLanguageID: Int) {
            val nWord = Word(0, nativeLanguageID, nativeWord)
            val fWord = Word(0, foreignLanguageID, foreignWord)

            val wordDao= db.getWordDao()

            val pair = Pair(0, wordDao.insert(nWord).toInt(), wordDao.insert(fWord).toInt())
            db.getWordPairDao().insert(pair)
        }
    }
}