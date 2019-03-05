package com.sigma.sudokuworld.persistence.db.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.sigma.sudokuworld.persistence.db.entities.Set

@Dao
interface SetDao {

    @Query("SELECT * FROM sets")
    fun getAll(): List<Set>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg set: Set)

    @Query("DELETE FROM sets")
    fun deleteAll()
}