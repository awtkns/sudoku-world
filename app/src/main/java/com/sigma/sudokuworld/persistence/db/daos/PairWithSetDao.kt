package com.sigma.sudokuworld.persistence.db.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.sigma.sudokuworld.persistence.db.entities.Pair
import com.sigma.sudokuworld.persistence.db.entities.PairWithSet
import com.sigma.sudokuworld.persistence.db.entities.Word
import com.sigma.sudokuworld.persistence.db.views.WordPair

@Dao
interface PairWithSetDao {

    @Query("""
        SELECT wscr.pairID,
        n.wordID as n_wordID, n.word as n_word, n.languageID as n_languageID,
        f.wordID as f_wordID, f.word as f_word, f.languageID as f_languageID,
        nlang.name as n_lang_name,
        flang.name as f_lang_name
        FROM word_set_cross_reference as wscr
        INNER join word_pairs as p on wscr.pairID == p.pairID
        INNER JOIN words as n on p.nativeWordID == n.wordID
        INNER JOIN words as f on p.foreignWordID == f.wordID
        INNER JOIN languages as nlang on n.languageID == nlang.languageID
        INNER JOIN languages as flang on f.languageID == flang.languageID
        WHERE wscr.setID == :setID
    """)
    fun getAllWordPairsInSet(setID: Long): List<WordPair>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg pairWithSets: PairWithSet)

    @Query("DELETE FROM word_set_cross_reference")
    fun deleteAll()
}