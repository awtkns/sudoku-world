package com.sigma.sudokuworld;

import android.util.Log;

class GameModel {

    static final int SUDOKU_ROOT_SIZE = 2;
    static final int SUDOKU_SIZE = SUDOKU_ROOT_SIZE * SUDOKU_ROOT_SIZE;

    private int[] cellValues;
    private int numFilledCells = 0;

    GameModel(){
        cellValues = new int[SUDOKU_SIZE * SUDOKU_SIZE];
    }

    void setValue(int x, int y, int number) {
        Log.d("Set value", String.format("X: %d Y: %d N: %d", x, y, number));
         if (cellValues[(y * SUDOKU_SIZE) + x] == 0 && number != 0)
             numFilledCells++;
         else if (cellValues[(y * SUDOKU_SIZE) + x] != 0 && number == 0)
             numFilledCells--;

        cellValues[(y * SUDOKU_SIZE) + x] = number;
    }

    int getValue(int x, int y) {
        return cellValues[(y * SUDOKU_SIZE) + x];
    }

    int[] getFilledCells() {
        int[] filledCells = new int[numFilledCells];
        int i = 0;

        for (int j = 0; j < cellValues.length; j++) {
            if (cellValues[j] != 0)
                filledCells[i++] = j;
        }

        return filledCells;
    }

    private void sampleValues() {
        for (int i : cellValues) {
            cellValues[i] = i + 1;
        }
    }

    static int cellNumToXPosition(int cellNumber) {
        return cellNumber % SUDOKU_SIZE;
    }

    static int cellNumToYPosition(int cellNumber) {
        return cellNumber / SUDOKU_SIZE;
    }
}
