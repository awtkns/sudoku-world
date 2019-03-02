package com.sigma.sudokuworld.persistence.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface WordPairDao {

    @Query("SELECT * FROM word_pairs")
    fun getAll(): List<WordPair>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg wordPairs: WordPair)

    @Query("DELETE FROM word_pairs")
    fun deleteAll()
}