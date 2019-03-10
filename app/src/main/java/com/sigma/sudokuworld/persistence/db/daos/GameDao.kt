package com.sigma.sudokuworld.persistence.db.daos

import android.arch.persistence.room.*
import com.sigma.sudokuworld.persistence.db.entities.Game

@Dao
interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(game: Game): Long

    @Update
    fun update(game: Game)

    @Query("DELETE FROM game_saves")
    fun deleteAll()

    @Query("SELECT * FROM game_saves")
    fun getAll(): List<Game>

    @Query("SELECT * FROM game_saves WHERE saveID == :saveID LIMIT 1")
    fun getGameSaveByID(saveID: Long): Game
}
