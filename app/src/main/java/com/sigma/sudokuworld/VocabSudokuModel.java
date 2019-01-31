package com.sigma.sudokuworld;

import android.util.SparseArray;

import java.util.HashMap;

public class VocabSudokuModel {

    private GameModel model;
    private SparseArray<String> nativeWordsMap;
    private SparseArray<String> foreignWordsMap;


    public VocabSudokuModel(String[] nativeWords, String[] foreignWords) {
        model = new GameModel();
        init(nativeWords, foreignWords);
    }

    public VocabSudokuModel(String[] nativeWords, String[] foreignWords, int[] startingPuzzle) {
        model = new GameModel(startingPuzzle);
        init(nativeWords, foreignWords);
    }

    //LMAO
    private void init(String[] nWords, String[] fWords) {
        nativeWordsMap = new SparseArray<>();
        nativeWordsMap.append(0, "");
        for(int i = 1; i <= nWords.length; i++) {
            nativeWordsMap.append(i, nWords[i]);
        }

        foreignWordsMap = new SparseArray<>();
        foreignWordsMap.append(0, "");
        for(int i = 1; i <= fWords.length; i++) {
            foreignWordsMap.append(i, fWords[i]);
        }
    }

    public String getCellString(int cellNumber) {
        return nativeWordsMap.valueAt(model.getCellValue(cellNumber));
    }

    public void setCellString(int cellNumber, String str) {
        model.setCellValue(cellNumber, nativeWordsMap.indexOfValue(str));
    }

    public boolean isLockedCell(int cellNumber) {
        return model.isLockedCell(cellNumber);
    }
}
