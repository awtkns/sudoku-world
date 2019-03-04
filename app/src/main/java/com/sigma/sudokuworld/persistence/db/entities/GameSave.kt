package com.sigma.sudokuworld.persistence.db.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.sigma.sudokuworld.game.GameDifficulty
import com.sigma.sudokuworld.game.GameMode

@Entity(tableName = "game_saves")
data class GameSave (
    @PrimaryKey(autoGenerate = true) var saveID: Int,
    var difficulty: GameDifficulty,
    var gameMode: GameMode,
    var cellValues: IntArray,
    var solutionValues: IntArray,
    var lockedCells: BooleanArray
)