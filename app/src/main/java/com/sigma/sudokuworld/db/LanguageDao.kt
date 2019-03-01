package com.sigma.sudokuworld.db

import android.arch.persistence.room.*

@Dao
interface LanguageDao {

    @Query("SELECT * FROM languages")
    fun getAll(): List<Language>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg languages: Language)

    @Query("DELETE FROM languages")
    fun deleteAll()
}