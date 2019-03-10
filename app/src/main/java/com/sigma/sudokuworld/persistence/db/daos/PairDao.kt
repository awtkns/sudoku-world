package com.sigma.sudokuworld.persistence.db.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.sigma.sudokuworld.persistence.db.entities.Pair
import com.sigma.sudokuworld.persistence.db.views.WordPair

@Dao
interface PairDao {

    @Query("SELECT * FROM word_pairs")
    fun getAll(): List<Pair>

    @Query("""
        SELECT pairID,
        n.wordID as n_wordID, n.word as n_word, n.languageID as n_languageID,
        f.wordID as f_wordID, f.word as f_word, f.languageID as f_languageID,
        nlang.name as n_lang_name,
        flang.name as f_lang_name
        FROM word_pairs
        INNER JOIN words as n on nativeWordID == n.wordID
        INNER JOIN words as f on nativeWordID == f.wordID
        INNER JOIN languages as nlang on n.languageID == nlang.languageID
        INNER JOIN languages as flang on f.languageID == flang.languageID
    """)
    fun getAllWordPairs(): List<WordPair>

    @Query("""
        SELECT pairID,
        n.wordID as n_wordID, n.word as n_word, n.languageID as n_languageID,
        f.wordID as f_wordID, f.word as f_word, f.languageID as f_languageID,
        nlang.name as n_lang_name,
        flang.name as f_lang_name
        FROM word_pairs
        INNER JOIN words as n on nativeWordID == n.wordID
        INNER JOIN words as f on nativeWordID == f.wordID
        INNER JOIN languages as nlang on n.languageID == nlang.languageID
        INNER JOIN languages as flang on f.languageID == flang.languageID
        WHERE pairID == :wordPairID
    """)
    fun getWordPair(wordPairID: Int): WordPair

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg wordPairs: Pair)

    @Query("DELETE FROM word_pairs")
    fun deleteAll()
}