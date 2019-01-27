package com.sigma.sudokuworld;

public class GameModel {

    public static final int SUDOKU_SIZE = 9;
    private int[] cellValues;

    public GameModel(){
        cellValues = new int[SUDOKU_SIZE * SUDOKU_SIZE];
        sampleValues();
    }

    public void setValue(int x, int y, int number) {
        cellValues[(y * SUDOKU_SIZE) + x] = number;
    }

    public int getValue(int x, int y) {
        return cellValues[(y * SUDOKU_SIZE) + x];
    }

    private void sampleValues() {
        for (int i = 0 ; i < SUDOKU_SIZE * SUDOKU_SIZE; i++) {
            cellValues[i] = i + 1;
        }
    }
}
