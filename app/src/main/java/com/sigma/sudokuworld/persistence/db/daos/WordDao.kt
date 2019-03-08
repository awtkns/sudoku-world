package com.sigma.sudokuworld.persistence.db.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.sigma.sudokuworld.persistence.db.entities.Word

@Dao
interface WordDao {

    @Query("SELECT * FROM words")
    fun getAll(): List<Word>

    @Query("SELECT * FROM words where wordID = :wordID")
    fun getWordByID(wordID: Int): Word

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg words: Word)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(word: Word): Long

    @Query("DELETE FROM words")
    fun deleteAll()

}