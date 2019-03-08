package com.sigma.sudokuworld.persistence.db.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.sigma.sudokuworld.persistence.db.entities.WordPair

@Dao
interface WordPairDao {

    @Query("SELECT * FROM word_pairs")
    fun getAll(): List<WordPair>

    @Query("SELECT * FROM word_pairs where wordPairID = :wordPairID")
    fun getWordPairByID(wordPairID: Int): WordPair

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg wordPairs: WordPair)

    @Query("DELETE FROM word_pairs")
    fun deleteAll()
}