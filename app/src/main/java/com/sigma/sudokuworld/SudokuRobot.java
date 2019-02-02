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
        for (int row = 0; row < mBoardLength; row++){
            for (int column = 0; column < mBoardLength; column++) {
                checkCandidates(row,column);
                mSudokuCells[row][column].pickCandidate();
            }
        }



    }

    private boolean checkConstraints(int row, int column) {
        SudokuCell cell = mSudokuCells[row][column];
        while(cell.pickCandidate()) {
            //checkCellRow(cellRow);
           // checkCellColumn(cellColumn);
            //checkCellBox(cellRow, cellColumn);
        }
        return true;
    }

    private void checkCandidates(int row, int column) {
        SudokuCell cell = mSudokuCells[row][column];

        //Remove pre-existing row and column
        for ( int i = 0; i < mBoardLength; i++) {
            cell.removeCandidate(mSudokuCells[row][i].getValue());
            cell.removeCandidate(mSudokuCells[i][column].getValue());
        }


        //Remove pre-existing sub-section values
        //The below calculations will be coordinants for the top left index of each sub-section
        int subSectionRow = mSudokuSubsectionSize*(row / mSudokuSubsectionSize);
        int subSectionColumn = mSudokuSubsectionSize*(column / mSudokuSubsectionSize);
        for ( int i = 0; i < mSudokuSubsectionSize; i++) {
            for (int j =0; j < mSudokuSubsectionSize; j++) {
                cell.removeCandidate(mSudokuCells[subSectionRow + i][subSectionColumn + j].getValue());
            }
        }
    }


    //Returns a one dimensional array containing cell values at their respective index
    public int[] returnCellValues() {
            int[] cellValues = new int[mBoardSize];
            int cellValueIndex = 0;
            //Loops through and grabs the value of each element in the 2D Sudoku cell array
            for (int row = 0; row < mBoardLength; row++) {
                for(int column = 0; column < mBoardLength; column++) {
                    //mSudokuCells[row][column].pickCandidate();
                    cellValues[cellValueIndex] = mSudokuCells[row][column].getValue();
                    cellValueIndex++;
                }
            }
            return cellValues;
    }



}
