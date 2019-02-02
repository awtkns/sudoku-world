package com.sigma.sudokuworld;

public class SudokuRobot {
    private SudokuCell[][] mSudokuCells;
    private int mSudokuSubsectionSize;
    private int mBoardLength;
    private int mBoardSize;



    //Constructor
    public SudokuRobot(int sudokuSubsectionSize) {
        mSudokuSubsectionSize = sudokuSubsectionSize;
        mBoardLength = sudokuSubsectionSize * sudokuSubsectionSize;
        mBoardSize = (mBoardLength) * (mBoardLength);

        generateSudokuCells();
        generateBoard();
    }


    
    private void generateSudokuCells() {
        //Creates space for the two dimensional array of Sudoku cells
        mSudokuCells = new SudokuCell[mBoardLength][mBoardLength];

        //Loops through and puts a cell in each index of the 2D Sudoku cell array
        for (int row = 0; row < mBoardLength; row++) {
            for (int column = 0; column < mBoardLength; column++) {
                mSudokuCells[row][column] = new SudokuCell(mSudokuSubsectionSize);
            }
        }
    }



    public void generateBoard() {

    }



    //Returns a one dimensional array containing cell values at their respective index
    public int[] returnCellValues() {
            int[] cellValues = new int[mBoardSize];
            int cellValueIndex = 0;
            //Loops through and grabs the value of each element in the 2D Sudoku cell array
            for (int row = 0; row < mBoardLength; row++) {
                for(int column = 0; column < mBoardLength; column++) {
                    mSudokuCells[row][column].getCandidate();
                    cellValues[cellValueIndex] = mSudokuCells[row][column].getValue();
                    cellValueIndex++;
                }
            }
            return cellValues;
    }



}
