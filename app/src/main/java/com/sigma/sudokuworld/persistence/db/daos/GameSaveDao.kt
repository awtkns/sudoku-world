package com.sigma.sudokuworld.persistence.db.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.sigma.sudokuworld.persistence.db.entities.GameSave

@Dao
interface GameSaveDao {

    @Query("SELECT * FROM game_saves")
    fun getAll(): List<GameSave>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg gameSave: GameSave)

    @Query("DELETE FROM game_saves")
    fun deleteAll()
}
