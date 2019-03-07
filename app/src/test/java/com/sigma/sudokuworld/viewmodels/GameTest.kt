package com.sigma.sudokuworld.viewmodels

import com.sigma.sudokuworld.game.GameDifficulty
import com.sigma.sudokuworld.game.GameMode
import com.sigma.sudokuworld.persistence.db.entities.Game
import org.junit.Test

import org.junit.Assert.*
import java.lang.IndexOutOfBoundsException

class GameTest {
    private val cells: IntArray = intArrayOf(1, 2, 4, 6, 8)
    private val solutions: IntArray = intArrayOf(1, 3, 5, 7, 9)
    private val locked: BooleanArray = booleanArrayOf(true, false, false, false, false)
    private val game = Game(1, 1, GameDifficulty.MEDIUM, GameMode.NUMBERS, cells, solutions, locked)

    @Test(expected = IndexOutOfBoundsException::class)
    fun indexOutOfBoundsException() {
        val input = 6
        game.getCellValue(input)
    }

    @Test
    fun getCellValue() {
        val input = 4
        game.setCellValue(input, 7)
        assertEquals(7, game.getCellValue(input))
        assertNotEquals(8, game.getCellValue(input))
    }

    @Test
    fun getSolutionValue() {
        val input = 2
        assertEquals(5, game.getSolutionValue(input))
        assertNotEquals(4, game.getSolutionValue(input))
    }

    @Test
    fun isLocked() {
        val input = 1
        assertEquals(false, game.isLocked(input))
        assertNotEquals(true, game.isLocked(input))
    }
}