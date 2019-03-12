package com.sigma.sudokuworld.persistence.db.utils

import com.sigma.sudokuworld.persistence.db.AppDatabase
import com.sigma.sudokuworld.persistence.db.entities.*
import com.sigma.sudokuworld.persistence.db.entities.Set

abstract class DatabaseInitializer {

    //Kotlin equivalent to static
    companion object {

        fun initLanguages(db: AppDatabase) {
            val english = Language(0, "English", "en")
            val french = Language(0, "French", "fr")

            val languageDao = db.getLanguageDao()

            if (languageDao.getLanguageByCode("en") == null) languageDao.insert(english)
            if (languageDao.getLanguageByCode("fr") == null) languageDao.insert(french)

        }

        fun populateDatabase(db: AppDatabase) {

            val words = arrayOf(
                    Word(1, 1, "Red"),
                    Word(2, 1, "Pink"),
                    Word(3, 1, "Green"),
                    Word(4, 1, "Purple"),
                    Word(5, 1, "Yellow"),
                    Word(6, 1, "White"),
                    Word(7, 1, "Black"),
                    Word(8, 1, "Brown"),
                    Word(9, 1, "Blue"),
                    Word(10, 2, "Rouge"),
                    Word(11, 2, "Rose"),
                    Word(12, 2, "Vert"),
                    Word(13, 2, "Violet"),
                    Word(14, 2, "Jaune"),
                    Word(15, 2, "Blanc"),
                    Word(16, 2, "Noir"),
                    Word(17, 2, "Marron"),
                    Word(18, 2, "Bleu")
            )

            val pairs = arrayOf(
                    Pair(1, 1, 10),
                    Pair(2, 2, 11),
                    Pair(3, 3, 12),
                    Pair(4, 4, 13),
                    Pair(5, 5, 14),
                    Pair(6, 6, 15),
                    Pair(7, 7, 16),
                    Pair(8, 8, 17),
                    Pair(9, 9, 18)
            )

            val set = Set(1, false,"French Colours", "Learn your french colours")

            val pairsWithSet = arrayOf(
                    PairWithSet(1, 1),
                    PairWithSet(1, 2),
                    PairWithSet(1, 3),
                    PairWithSet(1, 4),
                    PairWithSet(1, 5),
                    PairWithSet(1, 6),
                    PairWithSet(1, 7),
                    PairWithSet(1, 8),
                    PairWithSet(1, 9)
            )

            db.getWordDao().insert(*words)
            db.getPairDao().insert(*pairs)
            db.getSetDao().insert(set)
            db.getPairWithSetDao().insert(*pairsWithSet)
        }

        fun deleteAll(db: AppDatabase) {
            db.getLanguageDao().deleteAll()
            db.getWordDao().deleteAll()
            db.getPairDao().deleteAll()
            db.getSetDao().deleteAll()
            db.getPairWithSetDao().deleteAll()
        }
    }
}