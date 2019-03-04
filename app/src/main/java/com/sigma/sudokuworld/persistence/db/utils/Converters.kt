package com.sigma.sudokuworld.persistence.db.utils

import android.arch.persistence.room.TypeConverter
import com.sigma.sudokuworld.game.GameDifficulty
import com.sigma.sudokuworld.game.GameMode

class Converters {

    @TypeConverter
    fun difficultyToString(difficulty: GameDifficulty?): String? {
         return difficulty.toString()
    }

    @TypeConverter
    fun stringToDifficulty(string: String?): GameDifficulty? {
        return GameDifficulty.fromString(string)
    }

    @TypeConverter
    fun gameModeToString(gameMode: GameMode?): String? {
        return gameMode.toString()
    }

    @TypeConverter
    fun stringToGameMode(string: String?): GameMode? {
        return GameMode.fromString(string)
    }
}