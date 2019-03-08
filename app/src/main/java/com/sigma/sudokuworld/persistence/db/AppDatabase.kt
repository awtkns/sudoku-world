package com.sigma.sudokuworld.persistence.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.sigma.sudokuworld.persistence.db.daos.*
import com.sigma.sudokuworld.persistence.db.entities.*
import com.sigma.sudokuworld.persistence.db.entities.Set
import com.sigma.sudokuworld.persistence.db.utils.Converters
import com.sigma.sudokuworld.persistence.db.utils.DatabaseInitializer

/**
 * Creates the builds the database and ensure that only one is present
 * Written in kotlin
 */
@Database(
        version = 7,
        entities = [
            Language::class,
            Word::class,
            WordPair::class,
            Set::class,
            WordSet::class,
            Game::class
        ])
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getLanguageDao(): LanguageDao
    abstract fun getWordDao(): WordDao
    abstract fun getWordPairDao(): WordPairDao
    abstract fun getSetDao(): SetDao
    abstract fun getWordSetDao(): WordSetDao
    abstract fun getGameSaveDao(): GameSaveDao

    //Singleton
    companion object {
        @Volatile private var instance: AppDatabase? = null

        //Returns the database
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = buildDB(context)
                instance = buildDB(context)

                DatabaseInitializer.populateDatabase(instance!!)
            }

            return instance!!
        }

        //Should only be called if the database doesn't exist
        private fun buildDB(context: Context): AppDatabase {
            return Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sudokuDB")
                    .allowMainThreadQueries()   //TODO: DON'T RUN ON MAIN THREAD
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }
}