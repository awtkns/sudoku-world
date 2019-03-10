package com.sigma.sudokuworld.persistence.db.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.sigma.sudokuworld.persistence.db.entities.Pair
import com.sigma.sudokuworld.persistence.db.entities.PairWithSet
import com.sigma.sudokuworld.persistence.db.entities.Word

@Dao
interface PairWithSetDao {

    @Query("SELECT * FROM word_set_cross_reference as cr INNER JOIN word_pairs as p on cr.pairID == p.pairID WHERE setID = :setID")
    fun getAllWordsInSet(setID: Int): List<Pair>

    @Query("""
        SELECT nw.wordID, nw.languageID, nw.word FROM word_set_cross_reference as cf
        INNER JOIN word_pairs as p ON p.pairID == cf.pairID
        INNER JOIN words as nw ON p.nativeWordID == nw.wordID
        WHERE cf.setID == :setID
        ORDER BY cf.pairID ASC
        """)
    fun getAllNativeWordsInSet(setID: Int): List<Word>

    @Query("""
        SELECT fw.wordID, fw.languageID, fw.word  FROM word_set_cross_reference as cf
        INNER JOIN word_pairs as p ON p.pairID == cf.pairID
        INNER JOIN words as fw ON p.foreignWordID == fw.wordID
        WHERE cf.setID == :setID
        ORDER BY cf.pairID ASC
        """)
    fun getAllForeignWordsInSet(setID: Int): List<Word>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg pairWithSets: PairWithSet)

    @Query("DELETE FROM word_set_cross_reference")
    fun deleteAll()
}