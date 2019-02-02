package com.sigma.sudokuworld;

import android.util.SparseArray;

import java.util.HashMap;

/**
 * This class acts as an adapter allowing to the sudoku game to be played with words instead of numbers
 */
class VocabSudokuModel {

    private GameModel model;
    private String[] foreignWords;
    private String[] nativeWords;
    private SparseArray<String> nativeWordsMap;
    private SparseArray<String> foreignWordsMap;

    VocabSudokuModel(String[] nativeWords, String[] foreignWords) {
        model = new GameModel();
        this.nativeWords = nativeWords;
        this.foreignWords = foreignWords;
        init();
    }

    VocabSudokuModel(String[] nativeWords, String[] foreignWords, int[] startingPuzzle) {
        model = new GameModel(startingPuzzle);
        this.nativeWords = nativeWords;
        this.foreignWords = foreignWords;
        init();
    }

    private void init() {
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
        return nativeWordsMap.valueAt(model.getCellValue(cellNumber));
    }

    void setCellString(int cellNumber, String str) {
        model.setCellValue(cellNumber, nativeWordsMap.indexOfValue(str));
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

    boolean isInitialCell(int cellNumber) {
        return model.isLockedCell(cellNumber);
    }
}
