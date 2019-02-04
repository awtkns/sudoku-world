package com.sigma.sudokuworld;

import android.util.SparseArray;

import java.util.HashMap;

/**
 * This class acts as an adapter allowing to the sudoku game to be played with words instead of numbers
 */
class VocabSudokuModel {

    private GameModel model;
    private SparseArray<String> nativeWordsMap;
    private SparseArray<String> foreignWordsMap;

    VocabSudokuModel(String[] nativeWords, String[] foreignWords) {
        model = new GameModel(3);
        init(nativeWords, foreignWords);
    }

    //LMAO
    private void init(String[] nWords, String[] fWords) {
        nativeWordsMap = new SparseArray<>();
        nativeWordsMap.append(0, "");
        for(int i = 0; i < nWords.length; i++) {
            nativeWordsMap.append(i + 1, nWords[i]);
        }

        foreignWordsMap = new SparseArray<>();
        foreignWordsMap.append(0, "");
        for(int i = 0; i < fWords.length; i++) {
            foreignWordsMap.append(i + 1, fWords[i]);
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

    //Returns the index of the first incorrect cell or -1 if the board is solved
    public int checkGame(){
        return model.isGameWon();
    }
    boolean isInitialCell(int cellNumber) {
        return model.isLockedCell(cellNumber);
    }
}
