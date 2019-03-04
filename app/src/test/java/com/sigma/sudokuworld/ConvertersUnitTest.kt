package com.sigma.sudokuworld

import com.sigma.sudokuworld.game.GameDifficulty
import com.sigma.sudokuworld.game.GameMode
import com.sigma.sudokuworld.persistence.db.utils.Converters
import org.junit.Assert.*

import org.junit.Test

import java.lang.Exception

class ConvertersUnitTest {
    private val converters = Converters()

    @Test
    @Throws(Exception::class)
    fun booleanArrayConvert() {
        val booleanArray: BooleanArray = booleanArrayOf(true, false, false, true)
        val booleansString = converters.booleanArrayToString(booleanArray)

        val convertedArray = converters.stringToBooleanArray(booleansString)
        assertTrue(convertedArray!!.contentEquals(booleanArray))
    }

    @Test
    @Throws(Exception::class)
    fun intArrayConvert() {
        val intArray: IntArray = intArrayOf(0, 2, 6, 4)
        val intsString = converters.intArrayToString(intArray)

        val convertedArray = converters.stringToIntArray(intsString)
        assertTrue(convertedArray!!.contentEquals(intArray))
    }

    @Test
    @Throws(Exception::class)
    fun gameModeConvert() {
        val nativeMode = GameMode.NATIVE
        val foreignMode = GameMode.FOREIGN
        val numbersMode = GameMode.NUMBERS

        assertEquals(nativeMode, converters.stringToGameMode(converters.gameModeToString(nativeMode)))
        assertEquals(foreignMode, converters.stringToGameMode(converters.gameModeToString(foreignMode)))
        assertEquals(numbersMode, converters.stringToGameMode(converters.gameModeToString(numbersMode)))
    }

    @Test
    @Throws(Exception::class)
    fun difficultyConvert() {
        val hard = GameDifficulty.HARD
        val medium = GameDifficulty.MEDIUM
        val easy = GameDifficulty.EASY

        assertEquals(hard, converters.stringToDifficulty(converters.difficultyToString(hard)))
        assertEquals(medium, converters.stringToDifficulty(converters.difficultyToString(medium)))
        assertEquals(easy, converters.stringToDifficulty(converters.difficultyToString(easy)))
        assertNotEquals(easy, converters.stringToDifficulty(converters.difficultyToString(hard)))
    }
}