package com.sigma.sudokuworld.persistence.db.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.sigma.sudokuworld.persistence.db.entities.Pair
import com.sigma.sudokuworld.persistence.db.views.WordPair

@Suppress("SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection")
@Dao
interface PairDao {

    @Query("SELECT * FROM word_pairs")
    fun getAll(): List<Pair>

    @Suppress("SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection")
    @Query("""
        SELECT pairID,
        n.wordID as n_wordID, n.word as n_word, n.languageID as n_languageID,
        f.wordID as f_wordID, f.word as f_word, f.languageID as f_languageID,
        nlang.name as n_lang_name,
        flang.name as f_lang_name
        FROM word_pairs
        INNER JOIN words as n on nativeWordID == n.wordID
        INNER JOIN words as f on foreignWordID == f.wordID
        INNER JOIN languages as nlang on n.languageID == nlang.languageID
        INNER JOIN languages as flang on f.languageID == flang.languageID
    """)
    fun getAllWordPairs(): LiveData<List<WordPair>>

    @Query("""
        SELECT pairID,
        n.wordID as n_wordID, n.word as n_word, n.languageID as n_languageID,
        f.wordID as f_wordID, f.word as f_word, f.languageID as f_languageID,
        nlang.name as n_lang_name,
        flang.name as f_lang_name
        FROM word_pairs
        INNER JOIN words as n on nativeWordID == n.wordID
        INNER JOIN words as f on foreignWordID == f.wordID
        INNER JOIN languages as nlang on n.languageID == nlang.languageID
        INNER JOIN languages as flang on f.languageID == flang.languageID
        WHERE pairID == :wordPairID
    """)
    fun getWordPair(wordPairID: Long): WordPair?

    @Query("SELECT * FROM word_pairs WHERE nativeWordID = :nativeWordID AND foreignWordID = :foreignWordID")
    fun getPair(nativeWordID: Long, foreignWordID: Long): Pair?

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insert(vararg wordPairs: Pair)

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insert(wordPair: Pair): Long

    @Delete
    fun delete(vararg pair: Pair)

    @Query("DELETE FROM word_pairs WHERE pairID == :pairID")
    fun delete(pairID: Long)

    @Query("DELETE FROM word_pairs")
    fun deleteAll()
}