package com.sigma.sudokuworld.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface WordSetDao {

    @Query("SELECT * FROM word_set_cross_reference as cr INNER JOIN word_pairs as p on cr.wordPairID == p.wordPairID WHERE setID = :setID")
    fun getAllWordsInSet(setID: Int): List<WordPair>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg wordSets: WordSet)

    @Query("DELETE FROM word_set_cross_reference")
    fun deleteAll()
}