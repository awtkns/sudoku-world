package com.sigma.sudokuworld.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface WordDao {

    @Query("SELECT * FROM words")
    fun getAll(): List<Language>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg languages: Language)

    @Query("DELETE FROM words")
    fun deleteAll()

}