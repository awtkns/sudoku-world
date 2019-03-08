package com.sigma.sudokuworld.game.gen;

import android.os.Bundle;

import com.sigma.sudokuworld.game.GameDifficulty;
import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;

import org.junit.Test;
import static org.junit.Assert.*;

public class PuzzleGeneratorTest {
    private PuzzleGenerator puzzle = new PuzzleGenerator(3);

    @Test
    public void generatePuzzle() {
        Bundle b = puzzle.generatePuzzle(GameDifficulty.EASY);
        boolean[] locked = new boolean[81];
        assertEquals(81, b.size());
//        Bundle bundle = puzzle.generatePuzzle(GameDifficulty.EASY);
//        int value[] = bundle.getIntArray(KeyConstants.CELL_VALUES_KEY);
//        assertEquals(value, puzzle.generatePuzzle(GameDifficulty.EASY));
    }

    @Test
    public void isCellVisited() {
    }
}