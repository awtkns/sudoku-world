package com.sigma.sudokuworld;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.Math;


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
        generatePlayableBoard(80);
    }


    public void clearBoard() {
        //Reset solution array
        for (int i = 0; i < mBoardLength; i++){
            mSolutionValues[i] = 0;
        }

        //Reset every cell in cell list
        for (int row = 0; row < mBoardLength; row++) {
            for (int column = 0; column < mBoardLength; column++) {
                mSudokuCells[row][column].clearCurrValue();
                mSudokuCells[row][column].changeLockValue(false);
            }
        }
    }

    //Takes the row and column of a cell and removes all values from its candidate list that it
    //cannot take
    private void scanCandidates(int row, int column) {
        SudokuCell cell = mSudokuCells[row][column];

        //Remove pre-existing row and column
        for ( int i = 0; i < mBoardLength; i++) {
            cell.removeCandidate(mSudokuCells[row][i].getCurrValue());
            cell.removeCandidate(mSudokuCells[i][column].getCurrValue());
        }


        //Remove pre-existing sub-section values
        //The below calculations will be coordinants for the top left index of each sub-section
        int subSectionRow = mSudokuSubsectionSize*(row / mSudokuSubsectionSize);
        int subSectionColumn = mSudokuSubsectionSize*(column / mSudokuSubsectionSize);
        for ( int i = 0; i < mSudokuSubsectionSize; i++) {
            for (int j =0; j < mSudokuSubsectionSize; j++) {
                cell.removeCandidate(mSudokuCells[subSectionRow + i][subSectionColumn + j].getCurrValue());
            }
        }
    }


    public boolean solveBoard() {
        //For each cell, check what values it can take and make it take a random value
        //If it has no possible values, go to the previous cell and change its value and move on
        //Keep backtracking if all cells do not have any possible values
        //Algorithm ends when we go past the last cell
        boolean isBacktracking = false;
        int index = 0;
        while (index < mBoardSize) {
            int row = index / mBoardLength;
            int column = index % mBoardLength;

            //If we've backtracked to before the first node, return false
            if (index < 0) {
                return false;}

            //Cell must NOT be locked to change its value
            //If this cell has not had a value placed yet, reset its candidate list
            //This is for the situation where a node is revisited after being backtracked to
            //And therefore needs a fresh candidate list to find potential values on
            if (mSudokuCells[row][column].getCurrValue() == 0) {
                mSudokuCells[row][column].resetCandidateList();
            }

            scanCandidates(row, column);

            //Check if the cell is locked
            //If it is move to the next cell which depends on if we're backtracking or not
            if (mSudokuCells[row][column].isLocked()){
                //I had to put a statement in here in order for this to be checked
                //Currently doesn't do anything and skips the next two checks if the cell is locked
                int j =0;
                }

            //If candidate is picked, we're no longer backtracking and we move to next cell
            else if (mSudokuCells[row][column].pickCandidate()) {
                isBacktracking = false;
            }

            else {
                //Candidate is not picked, set cell value to zero and start backtracking
                mSudokuCells[row][column].clearCurrValue();
                isBacktracking = true;
            }


            //Change index based on whether or not we are backtracking
            if (isBacktracking) { index--; }
            else { index ++; }
        }

        //After we have found a solution, we lock every cell in place
        for (int row = 0; row < mBoardLength; row++) {
            for (int column = 0; column < mBoardLength; column++) {
                mSudokuCells[row][column].changeLockValue(true);
            }
        }

        return true;
    }


    public void generatePlayableBoard(int maxCycleLength) {
        //Keeps track of how loops have been iterated and what cells are emptied.
        //Works by emptying a node and then checking if the solution is unique
        //If a solution can still be generated with that node not being able to take its previous
        //value then we cannot delete that node
        int size = Math.min(mBoardSize, maxCycleLength);
        SudokuCell[] deletedNodeList = new SudokuCell[size];
        SudokuCell[] visitedCellList = new SudokuCell[size];
        int deletedNodeIndex = 0;

        for (int cycleCounter = 0; cycleCounter < maxCycleLength; cycleCounter++) {
            //Finding a random cell to empty
            int row = ThreadLocalRandom.current().nextInt(0, mBoardLength);
            int column = ThreadLocalRandom.current().nextInt(0, mBoardLength);
            SudokuCell cell = mSudokuCells[row][column];

            //Only delete and check cell if it HASN'T been visited
            if (!isCellVisited(size, visitedCellList, cell)){
                //Make sure the cell cannot re-get its current value
                int restrictedValue = cell.getCurrValue();
                cell.setRestrictedValue(restrictedValue);

                deletedNodeList[deletedNodeIndex] = cell;
                //Reseting all cells that have been emptied
                for (int i = 0; i <= deletedNodeIndex; i++) {
                    deletedNodeList[i].clearCurrValue();
                }

                //If there is no solutions now
                if(!solveBoard()) {
                    deletedNodeIndex++;
                }
                else {
                    cell.changeCurrValue(restrictedValue);
                }

                //Reset the cell's restricted value
                cell.setRestrictedValue(-1);
            }
        }

        //Solving the board places values in our deleted nodes
        //This is so that the value of the nodes will get reset
        for (int i = 0; i < deletedNodeIndex; i++) {
            deletedNodeList[i].clearCurrValue();
            deletedNodeList[i].resetCandidateList();
        }
    }
    private boolean isCellVisited(int size, SudokuCell[] visitedCellList, SudokuCell cell) {
        for (int i = 0; i < size; i++) {
            if (visitedCellList[i] == cell) {return true;}
        }
        return false;
    }

    //Places solution values on the board
    private void rePlaceBoard() {
        for (int index = 0; index < mBoardSize; index++) {
            int row = index / mBoardLength;
            int column = index % mBoardLength;
            mSudokuCells[row][column].changeCurrValue(mSolutionValues[index]);
        }
    }

    //Takes in solutions and checks if it matches the robot's (right) solutions
    public boolean isSolved(int[] userSolution) {
        for (int i = 0; i < mBoardSize; i++) {
            if (userSolution[i] != mSolutionValues[i]) {
                return false; }
        }
        return true;
    }


    //Returns a one dimensional array containing cell values at their respective index
    public int[] returnCellValues() {
            int[] cellValues = new int[mBoardSize];
            int cellValueIndex = 0;
            //Loops through and grabs the value of each element in the 2D Sudoku cell array
            for (int row = 0; row < mBoardLength; row++) {
                for(int column = 0; column < mBoardLength; column++) {
                    //mSudokuCells[row][column].pickCandidate();
                    cellValues[cellValueIndex] = mSudokuCells[row][column].getCurrValue();
                    cellValueIndex++;
                }
            }
            return cellValues;
    }
    public int[] returnSolutionValues() {
        return mSolutionValues;
    }
}
