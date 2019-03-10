package com.sigma.sudokuworld.persistence.db.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import android.util.Log
import com.sigma.sudokuworld.game.GameDifficulty
import com.sigma.sudokuworld.game.GameMode
import java.lang.IndexOutOfBoundsException

@Entity(tableName = "game_saves")
data class Game (
        @PrimaryKey(autoGenerate = true) var saveID: Int,
        @ForeignKey(
                entity = Set::class,
                parentColumns = ["setID"],
                childColumns = ["setID"],
                onDelete = CASCADE)
        var setID: Long,
        var difficulty: GameDifficulty,
        var gameMode: GameMode,
        var cellValues: IntArray,
        var solutionValues: IntArray,
        var lockedCells: BooleanArray
) {
    fun setCellValue(cellNumber: Int, value: Int) = try {
        cellValues[cellNumber] = value
    } catch(e: IndexOutOfBoundsException) {
        Log.wtf("Game", "Attempting to insert into an invalid index")
    }

    fun getCellValue(cellNumber: Int): Int {
        return cellValues[cellNumber]
    }

    fun getSolutionValue(cellNumber: Int): Int {
        return solutionValues[cellNumber]
    }

    fun isLocked(cellNumber: Int): Boolean {
        return lockedCells[cellNumber]
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Game

        if (saveID != other.saveID) return false
        if (setID != other.setID) return false
        if (difficulty != other.difficulty) return false
        if (gameMode != other.gameMode) return false
        if (!cellValues.contentEquals(other.cellValues)) return false
        if (!solutionValues.contentEquals(other.solutionValues)) return false
        if (!lockedCells.contentEquals(other.lockedCells)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = saveID
        result = 31 * result + setID.hashCode()
        result = 31 * result + difficulty.hashCode()
        result = 31 * result + gameMode.hashCode()
        result = 31 * result + cellValues.contentHashCode()
        result = 31 * result + solutionValues.contentHashCode()
        result = 31 * result + lockedCells.contentHashCode()
        return result
    }
}