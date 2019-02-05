package com.sigma.sudokuworld;

import android.util.Log;

import java.util.Arrays;

class GameModel {

    private final int sudokuSubsectionSize;
    private final int sudokuBoardLength;
    private final int sudokuBoardSize;

    //The cell values for all cells in the game
    private int[] cellValues;
    private int[] solutionCellValues;

    //True if the cell value cant be changed (ie: cell that was generated for the start of game)
    private boolean[] lockedCells;

    //Generates Puzzle from robot
    GameModel(int subsectionSize){
        //Figuring out what type of Sudoku game is being played
        sudokuSubsectionSize = subsectionSize;
        sudokuBoardLength = sudokuSubsectionSize * sudokuSubsectionSize;
        sudokuBoardSize = sudokuBoardLength * sudokuBoardLength;


        init();
    }

    GameModel(int subsectionSize, int[] puzzle, int[] solution, boolean[] initialCells) {
        //Figuring out what type of Sudoku game is being played
        sudokuSubsectionSize = subsectionSize;
        sudokuBoardLength = sudokuSubsectionSize * sudokuSubsectionSize;
        sudokuBoardSize = sudokuBoardLength * sudokuBoardLength;

        //Setting initial values
        try {
            cellValues = Arrays.copyOf(puzzle, puzzle.length);
            solutionCellValues = Arrays.copyOf(solution, solution.length);
            lockedCells = Arrays.copyOf(initialCells, initialCells.length);
        } catch (NullPointerException e) {
            Log.d("INIT", "GameModel: nullptr in game files\nMaking new puzzle");
            init();
        }
    }

    /**
     * Initialize a new puzzle
     */
    private void init() {
        cellValues = new int[sudokuBoardSize];
        solutionCellValues = new int[sudokuBoardSize];
        lockedCells = new boolean[sudokuBoardSize];
        Arrays.fill(lockedCells, false);


        //Grabbing initial values and solutions from the robot
        SudokuRobot sudokuRobot = new SudokuRobot(sudokuSubsectionSize);

        int[] tmp = sudokuRobot.returnCellValues();
        int[] initialValues = Arrays.copyOf(tmp, tmp.length);

        tmp = sudokuRobot.returnSolutionValues();
        solutionCellValues = Arrays.copyOf(tmp, tmp.length);


        //Setting up initial cells with values given from robot
        //Locks them in place if the value is not 0
        if (initialValues.length == sudokuBoardSize) {
            for (int i = 0; i < sudokuBoardSize; i++) {
                if (initialValues[i] != 0) {
                    cellValues[i] = initialValues[i];
                    lockedCells[i] = true;
                }
            }
        }
    }



    int getCellValue(int cellNumber) {
        return cellValues[cellNumber];
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
