package com.sigma.sudokuworld.persistence.db.daos

import android.arch.persistence.room.*
import com.sigma.sudokuworld.persistence.db.entities.Set

@Dao
interface SetDao {

    @Query("SELECT * FROM sets")
    fun getAll(): List<Set>

    @Query("SELECT * FROM sets where setID = :setID")
    fun getSetByID(setID:Int): Set

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg set: Set)

    @Delete
    fun delete(vararg set: Set)

    @Query("DELETE FROM sets")
    fun deleteAll()
}