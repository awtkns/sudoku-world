package com.sigma.sudokuworld.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 * Creates the builds the database and ensure that only one is present
 * Written in kotlin
 */
@Database(version = 2, entities = [Language::class, Word::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun getLanguageDao(): LanguageDao

    //Singleton
    companion object {
        @Volatile private var instance: AppDatabase? = null

        //Returns the database
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = buildDB(context)
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