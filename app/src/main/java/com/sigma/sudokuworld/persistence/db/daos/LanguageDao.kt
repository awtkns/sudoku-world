package com.sigma.sudokuworld.persistence.db.daos

import android.arch.persistence.room.*
import com.sigma.sudokuworld.persistence.db.entities.Language

@Dao
interface LanguageDao {

    @Query("SELECT * FROM languages")
    fun getAll(): List<Language>

    @Query("SELECT * FROM languages WHERE code = :languageCode")
    fun getLanguageByCode(languageCode: String): Language

    @Query("SELECT * FROM languages WHERE languageID = :languageID")
    fun getLanguageByID(languageID: Int): Language

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg languages: Language)

    @Query("DELETE FROM languages")
    fun deleteAll()
}