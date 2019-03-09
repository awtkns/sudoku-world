package com.sigma.sudokuworld.game.gen;

import android.os.Bundle;

import com.sigma.sudokuworld.game.GameDifficulty;
import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PuzzleGeneratorTest {
    private PuzzleGenerator puzzle = new PuzzleGenerator(3);
    Bundle b = puzzle.generatePuzzle(GameDifficulty.EASY);
    int cells[]= b.getIntArray(KeyConstants.CELL_VALUES_KEY);
    int solutions[]=b.getIntArray(KeyConstants.SOLUTION_VALUES_KEY);
    boolean locked[]=b.getBooleanArray(KeyConstants.LOCKED_CELLS_KEY);
    @Test
    public void generatePuzzle() {
        assertEquals(81, cells.length);
        assertEquals(81, solutions.length);
        assertEquals(81, locked.length);
    }

    @Test
    public void isValid(){
        int expected[] = solutions;
        assertArrayEquals(expected, solutions);
    }
}