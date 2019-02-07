package com.sigma.sudokuworld.VocabGame;

import java.util.Arrays;

class GameModel {

    private final int sudokuSubsectionSize;
    private final int sudokuBoardLength;
    private final int sudokuBoardSize;
    private final GameDifficulty gameDifficulty;

    //The cell values for all cells in the game
    private int[] cellValues;
    private int[] solutionCellValues;

    //True if the cell value cant be changed (ie: cell that was generated for the start of game)
    private boolean[] lockedCells;

    //Generates Puzzle from robot
    GameModel(int subsectionSize, GameDifficulty difficulty){
        //Figuring out what type of Sudoku game is being played
        gameDifficulty = difficulty;
        sudokuSubsectionSize = subsectionSize;
        sudokuBoardLength = sudokuSubsectionSize * sudokuSubsectionSize;
        sudokuBoardSize = sudokuBoardLength * sudokuBoardLength;


        init(difficulty);
    }

    GameModel(int subsectionSize, int[] puzzle, int[] solution, boolean[] initialCells, GameDifficulty difficulty) {
        //Figuring out what type of Sudoku game is being played
        gameDifficulty = difficulty;
        sudokuSubsectionSize = subsectionSize;
        sudokuBoardLength = sudokuSubsectionSize * sudokuSubsectionSize;
        sudokuBoardSize = sudokuBoardLength * sudokuBoardLength;

        //Setting initial values
        try {
            cellValues = puzzle;
            solutionCellValues = solution;
            lockedCells = initialCells;
        } catch (NullPointerException e) {
            e.printStackTrace();
            init(GameDifficulty.MEDIUM);
        }
    }

    /**
     * Initialize a new puzzle
     */
    private void init(GameDifficulty difficulty) {
        cellValues = new int[sudokuBoardSize];
        solutionCellValues = new int[sudokuBoardSize];
        lockedCells = new boolean[sudokuBoardSize];
        Arrays.fill(lockedCells, false);


        //Grabbing initial values and solutions from the robot
        SudokuRobot sudokuRobot = new SudokuRobot(sudokuSubsectionSize, difficulty);

        cellValues = sudokuRobot.returnCellValues();
        solutionCellValues = sudokuRobot.getSolutionValues();


        //Setting up initial cells with values given from robot
        //Locks them in place if the value is not 0
        if (cellValues.length == sudokuBoardSize) {
            for (int i = 0; i < sudokuBoardSize; i++) {
                if (cellValues[i] != 0) {
                    lockedCells[i] = true;
                }
            }
        }
    }



    int getCellValue(int cellNumber) {
        return cellValues[cellNumber];
    }
    boolean isCellCorrect(int cell){
        return solutionCellValues[cell] == cellValues[cell];
    }
    void setCellValue(int cellNumber, int value) {
        cellValues[cellNumber] = value;
    }

    int[] getAllCellValues() {
        return cellValues;
    }

    int[] getSolutionValues() {
        return solutionCellValues;
    }

    boolean[] getAllInitialCells() {
        return lockedCells;
    }

    GameDifficulty getGameDifficulty() {
        return gameDifficulty;
    }

    //Returns index of first incorrect cell or returns -1 if board is solved
    int isGameWon() {
        for (int i = 0; i < sudokuBoardSize; i++) {
            if (cellValues[i] != solutionCellValues[i]) {
                return i; }
        }
        return -1;
    }

    boolean isInitialCell(int cellNumber) {
        return lockedCells[cellNumber];
    }
}
