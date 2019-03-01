package com.sigma.sudokuworld.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 * Creates the data for the app
 * Written in kotlin
 */
@Database(version = 1, entities = [Language::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun languageDao(): LanguageDao

    //Singleton
    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = buildDB(context)
            }

            return instance!!
        }


        private fun buildDB(context: Context): AppDatabase {
            return Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sudokuDB").allowMainThreadQueries().build() //TODO: DON'T RUN ON MAIN THREAD
        }
    }
}