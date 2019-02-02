package com.sigma.sudokuworld;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class SudokuCell {
    private int mCurrValue;  //0 if no value is placed
    private int mSudokuSubsectionSize;
    //Shrinking list of possible values the cell may take
    private List<Integer> mCandidatesList;



    //Constructor
    public SudokuCell(int sudokuSubsectionSize) {
        mCurrValue = ThreadLocalRandom.current().nextInt(0, 9);
        mSudokuSubsectionSize = sudokuSubsectionSize;
        mCandidatesList = new ArrayList<Integer>();
        resetCandidateList();
    }



    //Randomly gets a possible candidate and removes it from the candidate list
    //If there are no possible candidates it returns 0
    public int getCandidate() {
        //No candidates
        if (mCandidatesList.size() == 0)
            return 0;

        //Candidates exist so a random index between 0 and MAX is selected
        int randomIndex = ThreadLocalRandom.current().nextInt(0, mCandidatesList.size() - 1);
        int candidate = mCandidatesList.get(randomIndex);
        mCandidatesList.remove(randomIndex);
        return candidate;
    }



    //Clears the candidate list and sets it to the base candidate list
    public void resetCandidateList() {
        mCandidatesList.clear();
        createBaseCandidateList();
    }



    //Creates the list of candidates from 0 to N where N is the max number allowed
    //N is determined by the SUDOKU_ROOT_SIZE
    //If the board is 9x9, the SUDOKU_ROOT_SIZE = 3
    private void createBaseCandidateList() {
        int maxNumber = mSudokuSubsectionSize * mSudokuSubsectionSize;

        for (int i = 1; i <= maxNumber; i++) {
            mCandidatesList.add(i);
        }

    }



    public int getValue() {
        return mCurrValue;
    }
}
