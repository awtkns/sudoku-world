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
) {
    fun setCellValue(cellNumber: Int, value: Int) {
        cellValues[cellNumber] = value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameSave

        if (saveID != other.saveID) return false
        if (difficulty != other.difficulty) return false
        if (gameMode != other.gameMode) return false
        if (!cellValues.contentEquals(other.cellValues)) return false
        if (!solutionValues.contentEquals(other.solutionValues)) return false
        if (!lockedCells.contentEquals(other.lockedCells)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = saveID
        result = 31 * result + difficulty.hashCode()
        result = 31 * result + gameMode.hashCode()
        result = 31 * result + cellValues.contentHashCode()
        result = 31 * result + solutionValues.contentHashCode()
        result = 31 * result + lockedCells.contentHashCode()
        return result
    }
}