package com.sigma.sudokuworld;

import java.util.Arrays;

class GameModel {

    private final int SUDOKU_SIZE = 9;
    private final int SUDOKU_NUMBER_OF_CELLS;

    //The cell values for all cells in the game
    private int[] cellValues;

    //True if the cell value cant be changed (ie: cell that was generated for the start of game)
    private Boolean[] lockedCells;


    GameModel() {
        this(new SudokuRobot(3).returnCellValues());
    }

    GameModel(int[] initialValues){
        SUDOKU_NUMBER_OF_CELLS = SUDOKU_SIZE * SUDOKU_SIZE;
        cellValues = new int[SUDOKU_NUMBER_OF_CELLS];
        lockedCells = new Boolean[SUDOKU_NUMBER_OF_CELLS];
        Arrays.fill(lockedCells, false);

        //Seting up intial cells
        if (initialValues.length == SUDOKU_NUMBER_OF_CELLS) {
            for (int i = 0; i < SUDOKU_NUMBER_OF_CELLS; i++) {
                if (initialValues[i] != 0) {
                    cellValues[i] = initialValues[i];
                    lockedCells[i] = true;
                }
            }
        }
    }

    int getCellValue(int cellNumber) {
        return cellValues[cellNumber];
    }

    void setCellValue(int cellNumber, int val) {
        cellValues[cellNumber] = val;
    }

    int[] getAllCellValues() {
        return cellValues;
    }

    private boolean checkWinConditions() {
        //Check win conditions for row, col, and square here

        return false;
    }

    public boolean isGameWon() {
        return checkWinConditions();
    }

    public boolean isLockedCell(int cellNumber) {
        return lockedCells[cellNumber];
    }
}
