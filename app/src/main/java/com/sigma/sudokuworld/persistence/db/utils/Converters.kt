package com.sigma.sudokuworld.persistence.db.utils

import android.arch.persistence.room.TypeConverter
import android.renderscript.Sampler
import com.sigma.sudokuworld.game.GameDifficulty
import com.sigma.sudokuworld.game.GameMode

private const val ARRAY_DELIMITER = ','
private const val SIZE_DELIMITER = ':'

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

    @TypeConverter
    fun intArrayToString(intArray: IntArray?): String? {
        if (intArray == null) return ""
        var string = ""

        //Size of array
        string += "${intArray.size}$SIZE_DELIMITER"

        //"intArray[i] followed by a ','"
        for (i in intArray) {
            string += "$i$ARRAY_DELIMITER"
        }
        return string
    }

    @TypeConverter
    fun stringToIntArray(string: String?): IntArray? {
        if (string == "" || string == null) return null

        var arrString: String
        arrString = string


        val intArray = IntArray(arrString.substringBefore(SIZE_DELIMITER).toInt())
        arrString = arrString.substringAfter(SIZE_DELIMITER)
        for (i in intArray.indices) {
            intArray[i] = arrString.substringBefore(ARRAY_DELIMITER).toInt()
            arrString = arrString.substringAfter(ARRAY_DELIMITER)
        }

        return intArray;
    }

    @TypeConverter
    fun booleanArrayToString(booleanArray: BooleanArray?): String? {
        if (booleanArray == null) return ""
        var string = ""

        //Size of array
        string += "${booleanArray.size}$SIZE_DELIMITER"

        //"intArray[i] followed by a ','"
        for (b in booleanArray) {
            string += "$b$ARRAY_DELIMITER"
        }
        return string
    }

    @TypeConverter
    fun stringToBooleanArray(string: String?): BooleanArray? {
        if (string == "" || string == null) return null

        var arrString: String
        arrString = string


        val booleanArray = BooleanArray(arrString.substringBefore(SIZE_DELIMITER).toInt())
        arrString = arrString.substringAfter(SIZE_DELIMITER)
        for (i in booleanArray.indices) {
            booleanArray[i] = arrString.substringBefore(ARRAY_DELIMITER).toBoolean()
            arrString = arrString.substringAfter(ARRAY_DELIMITER)
        }

        return booleanArray;
    }
}