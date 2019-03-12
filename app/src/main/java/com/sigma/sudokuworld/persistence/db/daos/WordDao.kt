package com.sigma.sudokuworld.persistence.db.daos

import android.arch.persistence.room.*
import com.sigma.sudokuworld.persistence.db.entities.Word

@Dao
interface WordDao {

    @Query("SELECT * FROM words")
    fun getAll(): List<Word>

    @Query("SELECT * FROM words where wordID = :wordID")
    fun getWordByID(wordID: Long): Word?

    @Query("SELECT * FROM words where word = :word AND languageID = :languageID")
    fun getWord(word: String, languageID: Long): Word?

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insert(vararg words: Word)

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insert(word: Word): Long

    @Query("DELETE FROM words")
    fun deleteAll()

    @Query("DELETE FROM words WHERE wordID == :wordID")
    fun delete(wordID: Long)


}