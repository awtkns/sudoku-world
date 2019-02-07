package com.sigma.sudokuworld.VocabGame;

import android.util.SparseArray;

/**
 * This class acts as an adapter allowing to the sudoku game to be played with words instead of numbers
 */
public class VocabSudokuModel {

    private GameModel model;
    private GameMode gameMode;
    private String[] foreignWords;
    private String[] nativeWords;
    private SparseArray<String> nativeWordsMap;
    private SparseArray<String> foreignWordsMap;

    public VocabSudokuModel(String[] nativeWords, String[] foreignWords, int[] puzzle, int[] solution, boolean[] initialCells, GameMode gameMode, GameDifficulty difficulty) {
        model = new GameModel(3, puzzle, solution, initialCells, difficulty);
        initializeWordMaps(nativeWords, foreignWords);
        this.gameMode = gameMode;
    }

    public VocabSudokuModel(String[] nativeWords, String[] foreignWords, GameMode gameMode, GameDifficulty difficulty) {
        model = new GameModel(3, difficulty);
        initializeWordMaps(nativeWords, foreignWords);
        this.gameMode = gameMode;
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

    public String getCellString(int cellNumber) {
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
            //Native mode
            return nativeWordsMap.valueAt(model.getCellValue(cellNumber));
        }

        //gameMode == GameMode.foreignMode
        return foreignWordsMap.valueAt(model.getCellValue(cellNumber));
    }

    public void setCellString(int cellNumber, int value) {
        model.setCellValue(cellNumber, value);
    }


    public String getButtonString(int value){
        if (value == 0) { return "";}
        if (gameMode == GameMode.NUMBERS){ return String.valueOf(value); }

        else if (gameMode == GameMode.NATIVE){
            return foreignWordsMap.valueAt(value);
        }

        else{
            return nativeWordsMap.valueAt(value);
        }
    }


    public boolean isCellCorrect(int cell){
        return model.isCellCorrect(cell);
    }

    public String[] getForeignWords() {
        return foreignWords;
    }

    public String[] getNativeWords() {
        return nativeWords;
    }

    public int[] getCellValues() {
        return model.getAllCellValues();
    }

    public int[] getSolutionValues() {
        return model.getSolutionValues();
    }

    public boolean[] getLockedCells() {
        return model.getAllInitialCells();
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public GameDifficulty getGameDifficulty(){
        return model.getGameDifficulty();
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    //Returns the index of the first incorrect cell or -1 if the board is solved
    public int checkGame(){
        return model.isGameWon();
    }

    public boolean isLockedCell(int cellNumber) {
        return model.isInitialCell(cellNumber);
    }
}


