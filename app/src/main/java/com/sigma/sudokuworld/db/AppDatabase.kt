package com.sigma.sudokuworld.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * Creates the data for the app
 * Written in kotlin
 */
@Database(version = 1, entities = [Language::class])
abstract class AppDatabase : RoomDatabase() {
    //Work in progress
}