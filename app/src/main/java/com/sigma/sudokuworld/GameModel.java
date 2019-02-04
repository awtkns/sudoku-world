package com.sigma.sudokuworld;

import java.util.Arrays;

class GameModel {

    private final int sudokuSubsectionSize;
    private final int sudokuBoardLength;
    private final int sudokuBoardSize;

    //The cell values for all cells in the game
    private int[] cellValues;
    private int[] solutionCellValues;

    //True if the cell value cant be changed (ie: cell that was generated for the start of game)
    private Boolean[] lockedCells;


    GameModel(int subsectionSize){
        //Figuring out what type of Sudoku game is being played
        sudokuSubsectionSize = subsectionSize;
        sudokuBoardLength = sudokuSubsectionSize * sudokuSubsectionSize;
        sudokuBoardSize = sudokuBoardLength * sudokuBoardLength;


        cellValues = new int[sudokuBoardSize];
        solutionCellValues = new int[sudokuBoardSize];
        lockedCells = new Boolean[sudokuBoardSize];
        Arrays.fill(lockedCells, false);


        //Grabbing initial values and solutions from the robot
        SudokuRobot sudokuRobot = new SudokuRobot(sudokuSubsectionSize);
        int[] initialValues = sudokuRobot.returnCellValues();
        solutionCellValues = sudokuRobot.returnSolutionValues();


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

    public int getCellValue(int cellNumber) {
        return cellValues[cellNumber];
    }

    public void setCellValue(int cellNumber, int value) {
        cellValues[cellNumber] = value;
    }

    //Returns index of first incorrect cell or returns -1 if board is solved
    public int isGameWon() {
        for (int i = 0; i < sudokuBoardSize; i++) {
            if (cellValues[i] != solutionCellValues[i]) {
                return i; }
        }
        return -1;
    }

    public boolean isLockedCell(int cellNumber) {
        return lockedCells[cellNumber];
    }
}
