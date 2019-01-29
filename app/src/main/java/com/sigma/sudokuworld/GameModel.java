package com.sigma.sudokuworld;

class GameModel {

    final int SUDOKU_ROOT_SIZE;
    private final int SUDOKU_SIZE;
    private final int SUDOKU_NUMBER_OF_CELLS;

    private int[] cellValues;
    private int numFilledCells = 0;
    private boolean isGameWon;

    GameModel(int sudokuRootSize){
        SUDOKU_ROOT_SIZE = sudokuRootSize;
        SUDOKU_SIZE = SUDOKU_ROOT_SIZE * SUDOKU_ROOT_SIZE;
        SUDOKU_NUMBER_OF_CELLS = SUDOKU_SIZE * SUDOKU_SIZE;

        cellValues = new int[SUDOKU_NUMBER_OF_CELLS];
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
}
