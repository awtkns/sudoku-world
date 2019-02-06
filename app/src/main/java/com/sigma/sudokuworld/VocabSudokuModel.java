package com.sigma.sudokuworld;

import android.util.SparseArray;

/**
 * This class acts as an adapter allowing to the sudoku game to be played with words instead of numbers
 */
class VocabSudokuModel {

    private GameModel model;
    private GameMode gameMode;
    private String[] foreignWords;
    private String[] nativeWords;
    private SparseArray<String> nativeWordsMap;
    private SparseArray<String> foreignWordsMap;

    VocabSudokuModel(String[] nativeWords, String[] foreignWords, int[] puzzle, int[] solution, boolean[] initialCells, GameMode mode) {
        model = new GameModel(3, puzzle, solution, initialCells);
        initializeWordMaps(nativeWords, foreignWords);
        gameMode = mode;
    }

    VocabSudokuModel(String[] nativeWords, String[] foreignWords, GameMode mode) {
        model = new GameModel(3);
        initializeWordMaps(nativeWords, foreignWords);
        gameMode = mode;
    }

    private void initializeWordMaps(String[] nativeWords, String[] foreignWords) {
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
        //Normal Mode
        if (gameMode == GameMode.NUMBERS){
            //Makes the cell blank if its value is 0
            if (model.getCellValue(cellNumber) == 0)
            {
                return nativeWordsMap.valueAt(model.getCellValue(cellNumber));
            }

            return String.valueOf(model.getCellValue(cellNumber));
        }
        else if (gameMode == GameMode.NATIVE) {
            nativeWordsMap.valueAt(model.getCellValue(cellNumber));
        }

        //else if (gameMode == GameMode.FOREIGN){
        return foreignWordsMap.valueAt(model.getCellValue(cellNumber));}
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

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    //Returns the index of the first incorrect cell or -1 if the board is solved
    int checkGame(){
        return model.isGameWon();
    }

    boolean isInitialCell(int cellNumber) {
        return model.isInitialCell(cellNumber);
    }
}


