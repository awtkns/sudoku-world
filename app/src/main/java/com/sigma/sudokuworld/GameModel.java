package com.sigma.sudokuworld;

import java.util.Arrays;

class GameModel {

    final int SUDOKU_ROOT_SIZE;
    final int SUDOKU_SIZE;
    final int SUDOKU_NUMBER_OF_CELLS;

    private int[] cellValues;
    private Boolean[] lockedCells;
    private int numFilledCells = 0;
    private boolean isGameWon;

    GameModel(int sudokuRootSize) {
        this(sudokuRootSize, new int[0]);
    }

    GameModel(int sudokuRootSize, int[] initialValues){
        SUDOKU_ROOT_SIZE = sudokuRootSize;
        SUDOKU_SIZE = SUDOKU_ROOT_SIZE * SUDOKU_ROOT_SIZE;
        SUDOKU_NUMBER_OF_CELLS = SUDOKU_SIZE * SUDOKU_SIZE;

        cellValues = new int[SUDOKU_NUMBER_OF_CELLS];
        lockedCells = new Boolean[SUDOKU_NUMBER_OF_CELLS];
        if (initialValues.length == SUDOKU_NUMBER_OF_CELLS) {
            for (int i = 0; i < SUDOKU_NUMBER_OF_CELLS; i++) {
                if (initialValues[i] != 0) {
                    cellValues[i] = initialValues[i];
                    lockedCells[i] = true;
                    numFilledCells++;
                } else {
                    cellValues[i] = 0;
                    lockedCells[i] = false;
                }
            }
        }

        isGameWon = false;
    }

    public int getCellValue(int cellNumber) {
        return cellValues[cellNumber];
    }

    public void setCellValue(int cellNumber, int val) {
        cellValues[cellNumber] = val;
    }

    private boolean checkWinConditions() {
        if (numFilledCells != SUDOKU_NUMBER_OF_CELLS)
            return false;

        //Check win conditions for row, col, and square here

        return true;
    }

    public boolean isGameWon() {
        return isGameWon;
    }

    public boolean isLockedCell(int cellNumber) {
        return lockedCells[cellNumber];
    }
}
