package com.sigma.sudokuworld;

import android.util.SparseArray;

/**
 * This class acts as an adapter allowing to the sudoku game to be played with words instead of numbers
 */
class VocabSudokuModel {

    private GameModel model;

    private String[] foreignWords;
    private String[] nativeWords;
    private SparseArray<String> nativeWordsMap;
    private SparseArray<String> foreignWordsMap;

    VocabSudokuModel(String[] nativeWords, String[] foreignWords, int[] puzzle, int[] solution, boolean[] initialCells) {
        model = new GameModel(3, puzzle, solution, initialCells);
        init(nativeWords, foreignWords);
    }

    VocabSudokuModel(String[] nativeWords, String[] foreignWords) {
        model = new GameModel(3);
        init(nativeWords, foreignWords);
    }

    private void init(String[] nativeWords, String[] foreignWords) {
        this.nativeWords = nativeWords;
        this.foreignWords = foreignWords;

        nativeWordsMap = new SparseArray<>();
        nativeWordsMap.append(0, "");
        for(int i = 0; i < nativeWords.length; i++) {
            nativeWordsMap.append(i + 1, nativeWords[i]);
        }

        foreignWordsMap = new SparseArray<>();
        foreignWordsMap.append(0, "");
        for(int i = 0; i < foreignWords.length; i++) {
            foreignWordsMap.append(i + 1, foreignWords[i]);
        }
    }

    String getCellString(int cellNumber) {
        //return nativeWordsMap.valueAt(model.getCellValue(cellNumber));

        //Makes the cell blank if its value is 0
        if (model.getCellValue(cellNumber) == 0)
        {return nativeWordsMap.valueAt(model.getCellValue(cellNumber));}

        return String.valueOf(model.getCellValue(cellNumber));
    }

    void setCellString(int cellNumber, int value) {
        model.setCellValue(cellNumber, value);
    }

    String[] getAllForeignWords() {
        return foreignWords;
    }

    String[] getAllNativeWords() {
        return nativeWords;
    }

    int[] getAllCellValues() {
        return model.getAllCellValues();
    }

    int[] getSolutionValues() {
        return model.getSolutionValues();
    }

    boolean[] getAllIntialCells() {
        return model.getAllInitialCells();
    }

    //Returns the index of the first incorrect cell or -1 if the board is solved
    int checkGame(){
        return model.isGameWon();
    }

    boolean isInitialCell(int cellNumber) {
        return model.isInitialCell(cellNumber);
    }
}
