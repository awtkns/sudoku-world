package com.sigma.sudokuworld.persistence.db.daos

import android.arch.persistence.room.*
import com.sigma.sudokuworld.persistence.db.entities.GameSave

@Dao
interface GameSaveDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg gameSave: GameSave)

    @Update
    fun update(gameSave: GameSave)

    @Query("DELETE FROM game_saves")
    fun deleteAll()

    @Query("SELECT * FROM game_saves")
    fun getAll(): List<GameSave>

    @Query("SELECT * FROM game_saves WHERE saveID == :saveID LIMIT 1")
    fun getGameSaveByID(saveID: Int): GameSave
}
