package com.sigma.sudokuworld;

public class GameModel {

    public static final int SUDOKU_SIZE = 9;
    private int[] cellValues;
    private int numFilledCells = 0;

    public GameModel(){
        cellValues = new int[SUDOKU_SIZE * SUDOKU_SIZE];
    }

    public void setValue(int x, int y, int number) {
         if (cellValues[(y * SUDOKU_SIZE) + x] == 0 && number != 0)
             numFilledCells++;
         else if (cellValues[(y * SUDOKU_SIZE) + x] != 0 && number == 0)
             numFilledCells--;

        cellValues[(y * SUDOKU_SIZE) + x] = number;
    }

    public int getValue(int x, int y) {
        return cellValues[(y * SUDOKU_SIZE) + x];
    }

    public int[] getFilledCells() {
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

    public static int cellNumToXPosition(int cellNumber) {
        return cellNumber % SUDOKU_SIZE;
    }

    public static int cellNumToYPosition(int cellNumber) {
        return cellNumber / SUDOKU_SIZE;
    }
}
