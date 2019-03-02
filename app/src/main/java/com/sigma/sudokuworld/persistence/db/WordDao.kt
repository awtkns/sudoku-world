package com.sigma.sudokuworld.persistence.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface WordDao {

    @Query("SELECT * FROM words")
    fun getAll(): List<Word>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg words: Word)

    @Query("DELETE FROM words")
    fun deleteAll()

}