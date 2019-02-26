package com.sigma.sudokuworld.game;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//This is the object for each Sudoku cell
//It holds the cell value and the potential candidates it has for its values (When attempting to solve)
//While a cell is locked, its value cannot be changed
//Cell may hold a restricted value. This is for checking what nodes to delete (-1 if no restriction)
class SudokuCell {
    private int mCurrValue;  //0 if no value is placed
    private int mSudokuSubsectionSize;
    private int mRestrictedValue; //-1 if no restriction placed
    private boolean mLockValue;
    //Shrinking list of possible values the cell may take
    private List<Integer> mCandidatesList;


    //Constructor
    SudokuCell(int sudokuSubsectionSize) {
        mCurrValue = 0;
        mLockValue = false;
        mRestrictedValue = -1;
        mSudokuSubsectionSize = sudokuSubsectionSize;
        mCandidatesList = new ArrayList<>();
        resetCandidateList();
    }


    private void createBaseCandidateList() {
        //Creates the list of candidates from 0 to N where N is the max number allowed
        //N is determined by the SUDOKU_ROOT_SIZE
        //If the board is 9x9, the SUDOKU_ROOT_SIZE = 3
        int maxNumber = mSudokuSubsectionSize * mSudokuSubsectionSize;

        for (int i = 1; i <= maxNumber; i++) {
            mCandidatesList.add(i);
        }

    }
    boolean pickCandidate() {
        //Randomly gets a possible candidate and removes it from the candidate list
        //If there are no possible candidates it returns 0


        //No candidates
        if (mCandidatesList.size() == 0)
            return false;

        //Candidates exist so a random index between 0 and MAX is selected
        int randomIndex = randomInt(mCandidatesList.size());
        mCurrValue = mCandidatesList.get(randomIndex);
        mCandidatesList.remove(randomIndex);

        //If the restricted value is selected, pick another candidate
        if (mCurrValue == mRestrictedValue){
            return pickCandidate();}

        return true;
    }
    void removeCandidate(int value) {
        mCandidatesList.remove((Integer) value);
    }
    void resetCandidateList() {
        //Clears the candidate list and sets it to the base candidate list
        mCandidatesList.clear();
        createBaseCandidateList();
    }


    boolean isLocked() {
        return mLockValue;
    }
    void changeLockValue(boolean lockValue) {
        mLockValue = lockValue;
    }


    void setRestrictedValue(int restrictedValue) {
        mRestrictedValue = restrictedValue;
    }


    void clearCurrValue() {
        mCurrValue = 0;
        changeLockValue(false);
    }
    int getCurrValue() {
        return mCurrValue;
    }
    void changeCurrValue(int value) {
        mCurrValue = value;
        mLockValue = true;
    }

    private static int randomInt(int max){
        Random random = new Random();
        return random.nextInt(max);
    }
}
