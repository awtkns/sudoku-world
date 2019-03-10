package com.sigma.sudokuworld.persistence.db.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.sigma.sudokuworld.persistence.db.entities.Pair
import com.sigma.sudokuworld.persistence.db.views.WordPair

@Dao
interface WordPairDao {

    @Query("SELECT * FROM word_pairs")
    fun getAll(): List<Pair>

    @Query("""
        SELECT n.word as nativeWord, f.word as foreignWord,
        nlang.languageID as n_languageID, nlang.name as n_name, nlang.code as n_code,
        flang.languageID as f_languageID, flang.name as f_name, nlang.code as f_code
        FROM word_pairs
        INNER JOIN words as n on nativeWordID == n.wordID
        INNER JOIN words as f on nativeWordID == f.wordID
        INNER JOIN languages as nlang on n.languageID == nlang.languageID
        INNER JOIN languages as flang on f.languageID == flang.languageID
    """)
    fun getAllDetail(): List<WordPair>

    @Query("SELECT * FROM word_pairs where wordPairID = :wordPairID")
    fun getWordPairByID(wordPairID: Int): Pair

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg wordPairs: Pair)

    @Query("DELETE FROM word_pairs")
    fun deleteAll()
}