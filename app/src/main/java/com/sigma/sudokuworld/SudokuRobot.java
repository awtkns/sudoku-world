package com.sigma.sudokuworld;
import java.util.concurrent.ThreadLocalRandom;


//This object takes in the subsection size of a Sudoku and can solve boards, generate valid sudoku boards
//and check whether or not a given solution is the correct solution
public class SudokuRobot {
    private SudokuCell[][] mSudokuCells;
    private int mSudokuSubsectionSize;
    private int mBoardLength;
    private int mBoardSize;
    private int[] mSolutionValues;


    //Constructor
    public SudokuRobot(int sudokuSubsectionSize) {
        mSudokuSubsectionSize = sudokuSubsectionSize;
        mBoardLength = sudokuSubsectionSize * sudokuSubsectionSize;
        mBoardSize = (mBoardLength) * (mBoardLength);
        mSolutionValues = new int[mBoardSize];
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

        generateBoard();
    }


    public void generateBoard() {
        clearBoard();
        solveBoard();
        mSolutionValues = returnCellValues();
        generatePlayableBoard(70);

    }


    public void clearBoard() {
        //Reset solution array
        for (int i = 0; i < mBoardLength; i++){
            mSolutionValues[i] = 0;
        }

        //Reset every cell in cell list
        for (int row = 0; row < mBoardLength; row++) {
            for (int column = 0; column < mBoardLength; column++) {
                mSudokuCells[row][column].clearValue();
                mSudokuCells[row][column].changeLockValue(false);
            }
        }
    }


    public void solveBoard() {
        //For each cell, check what values it can take and make it take a random value
        //If it has no possible values, go to the previous cell and change its value and move on
        //Keep backtracking if all cells do not have any possible values
        //Algorithm ends when we go past the last cell

        int index = 0;
        while (index < mBoardSize) {
            int row = index / mBoardLength;
            int column = index % mBoardLength;
            //Cell must NOT be locked to change its value
            //If this cell has not had a value placed yet, reset its candidate list
            //This is for the situation where a node is revisited after being backtracked to
            //And therefore needs a fresh candidate list to find potential values on
            if (mSudokuCells[row][column].getValue() == 0) {
                mSudokuCells[row][column].resetCandidateList();
            }

            scanCandidates(row, column);
            //Candidate is picked so go to next cell
            if (mSudokuCells[row][column].isLocked()){
                index++;}
            else if (mSudokuCells[row][column].pickCandidate()) {
                index++;
            } else {
                //Candidate is not picked, set cell value to zero and go to previous cell
                mSudokuCells[row][column].clearValue();
                index--;
            }
        }

        //Lock every cell
        for (int row = 0; row < mBoardLength; row++) {
            for (int column = 0; column < mBoardLength; column++) {
                mSudokuCells[row][column].changeLockValue(true);
            }
        }
    }


    public void generatePlayableBoard(int cycleLength) {
        //Keeps track of how loops have been iterated and what cells are emptied.
        //Works by emptying a node and then checking if the solution is unique
        //If only one solution is generated, that node can be deleted
        int currCycleLength = 0;
        SudokuCell[] deletedNodeList = new SudokuCell[cycleLength];
        int deletedNodeIndex = 0;

        while (currCycleLength < cycleLength) {
            //Finding a random cell to empty
            int row = ThreadLocalRandom.current().nextInt(0, mBoardLength);
            int column = ThreadLocalRandom.current().nextInt(0, mBoardLength);
            SudokuCell cell = mSudokuCells[row][column];

            int cellValue = cell.getValue();
            cell.clearValue();
            cell.removeCandidate(cellValue);
            deletedNodeList[deletedNodeIndex] = cell;
            //Reseting all cells that have been emptied
            for (int i = 0; i < deletedNodeIndex; i++) {
                deletedNodeList[i].clearValue();
                deletedNodeList[i].resetCandidateList();
            }

            //If solution is unique move on to the next cell
            if(isSolutionUnique()) {
                deletedNodeIndex++; }

            currCycleLength++;
            rePlaceBoard();
        }

        for (int i = 0; i < deletedNodeIndex; i++) {
            deletedNodeList[i].clearValue();
        }
    }

    private void rePlaceBoard() {
        for (int index = 0; index < mBoardSize; index++) {
            int row = index / mBoardLength;
            int column = index % mBoardLength;
            mSudokuCells[row][column].changeValue(mSolutionValues[index]);
        }
    }

    private boolean isSolutionUnique() {
        for (int index = 0; index < mBoardSize; index++) {
            int row = index / mBoardLength;
            int column = index % mBoardLength;

            //Candidate is picked so go to next cell
            scanCandidates(row, column);
            if (mSudokuCells[row][column].isLocked()){
                index++;}
            else if (mSudokuCells[row][column].pickCandidate()) {
                index++;
            } else {
                return true;
            }
        }
        return isSolution(returnCellValues());
    }

    public boolean isSolution(int[] userSolution) {
        for (int i = 0; i < mBoardSize; i++) {
            if (userSolution[i] != mSolutionValues[i]) {
                return false; }
        }
        return true;
    }


    private void scanCandidates(int row, int column) {
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
