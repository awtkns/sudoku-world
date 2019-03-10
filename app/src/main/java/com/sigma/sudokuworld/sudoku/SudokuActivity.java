package com.sigma.sudokuworld.sudoku;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


import android.widget.Toast;

import com.sigma.sudokuworld.persistence.sharedpreferences.PersistenceService;
import com.sigma.sudokuworld.viewmodels.SudokuViewModel;
import com.sigma.sudokuworld.viewmodels.SudokuViewModelFactory;
import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.audio.SoundPlayer;
import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;

import java.util.List;

public abstract class SudokuActivity extends AppCompatActivity {

    protected SudokuGridView mSudokuGridView;
    protected int cellTouched;
    protected SudokuViewModel mSudokuViewModel;
    protected Button[] mInputButtons;
    private SoundPlayer mSoundPlayer;
    private int mSaveID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        if (savedInstanceState != null) {
            mSaveID = savedInstanceState.getInt(KeyConstants.SAVE_ID_KEY);
        } else {
            Intent intent = getIntent();
            mSaveID = intent.getIntExtra(KeyConstants.SAVE_ID_KEY, 1);
        }

        SudokuViewModelFactory sudokuViewModelFactory = new SudokuViewModelFactory(getApplication(), mSaveID);
        mSudokuViewModel = ViewModelProviders.of(this, sudokuViewModelFactory).get(SudokuViewModel.class);

        //Set up buttons
        initButtons();
        final Observer<List<String>> buttonLabelsObserver = new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                setButtonLabels(strings);
            }
        };
        mSudokuViewModel.getButtonLabels().observe(this, buttonLabelsObserver);

        //Initializing Sudoku grid
        mSudokuGridView = findViewById(R.id.sudokuGrid_view);
        mSudokuGridView.setOnTouchListener(onSudokuGridTouchListener);
        mSudokuGridView.setCellLabels(this, mSudokuViewModel.getCellLabels());
        mSudokuGridView.setRectangleMode(PersistenceService.loadRectangleModeEnabledSetting(this));

        mSoundPlayer = new SoundPlayer(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the current state of the Sudoku board
        outState.putInt(KeyConstants.SAVE_ID_KEY, mSaveID);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //When sudoku grid is touched
    private SudokuGridView.OnTouchListener onSudokuGridTouchListener = new SudokuGridView.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int eventAction = event.getAction();
            boolean touchHandled = false;

            //Through looking at every case, we can move the highlight to where our finger moves to
            switch (eventAction) {
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_DOWN:
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    //If touch in the bound of the grid
                    if (mSudokuGridView.getGridBounds().contains(x, y)) {

                        //Clear previous highlighted cell
                        mSudokuGridView.clearHighlightedCell();

                        //Cell that was touched
                        int cellNum = mSudokuGridView.getCellNumberFromCoordinates(x, y);
                        cellTouched = cellNum;

                        //If we have selected the incorrect cell, un highlight it
                        if (cellNum == mSudokuGridView.getIncorrectCell()) {
                            mSudokuGridView.clearIncorrectCell();
                        }

                        //Set new highlighted cell if its not a locked cell
                        if (!mSudokuViewModel.isLockedCell(cellNum)) {
                            mSudokuGridView.setHighlightedCell(cellNum);

                            //No long press
                            touchHandled = true;
                        }

                        //Force redraw view
                        mSudokuGridView.invalidate();
                        mSudokuGridView.performClick();
                    }
            }

            return touchHandled;
        }
    };

    private View.OnClickListener onButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            int buttonValue = 0;

            //Loop through all our possible buttons to see which button is clicked
            //Set buttonValue to the corresponding button
            for (int buttonIndex = 0; buttonIndex < 9; buttonIndex++) {
                if (button == mInputButtons[buttonIndex]){
                    buttonValue = buttonIndex + 1;
                    break;
                }
            }

            int cellNumber = mSudokuGridView.getHighlightedCell();

            //No cell is highlighted
            if (cellNumber == -1){
                mSoundPlayer.playEmptyButtonSound();
            } else {
                if (mSudokuViewModel.isCorrectValue(cellNumber, buttonValue) || !mSudokuViewModel.isHintsEnabled()) {
                    //Correct number is placed in cell
                    mSudokuGridView.clearHighlightedCell();
                    mSudokuGridView.clearIncorrectCell();
                    mSoundPlayer.playPlaceCellSound();
                } else {
                    //Incorrect value has been placed in cell
                    mSudokuGridView.setIncorrectCell(cellNumber);
                    mSoundPlayer.playWrongSound();
                }

                mSudokuViewModel.setCellValue(cellNumber, buttonValue);
            }
        }
    };

    public void onCheckAnswerPressed(View v) {
        //Check if cell is selected
        //If a cell is selected, check if that cell is correct
        int highlightedCell = mSudokuGridView.getHighlightedCell();
        if (highlightedCell != -1)
        {
            //Cell is right
            if (mSudokuViewModel.isCellCorrect(highlightedCell)){
                mSudokuGridView.clearHighlightedCell();
                mSudokuGridView.invalidate();
                mSoundPlayer.playCorrectSound();
                Toast.makeText(getBaseContext(),
                        "The selected cell is correct!",
                        Toast.LENGTH_LONG).show();
            }

            //Cell is wrong
            else {
                mSudokuGridView.setIncorrectCell(highlightedCell);
                mSoundPlayer.playWrongSound();

            }
            mSudokuGridView.invalidate();
            return;
        }

        //Checks if the answers are right and displays the first wrong cell (if any)
        int potentialIndex = mSudokuViewModel.getIncorrectCellNumber();
        //Clear highlights / what cell is selected for input
        mSudokuGridView.clearHighlightedCell();

        //Case where answer is correct
        if (potentialIndex == -1) {
            mSoundPlayer.playCorrectSound();
            Toast.makeText(getBaseContext(),
                    "Congratulations, You've Won!",
                    Toast.LENGTH_LONG).show();
        }

        //Case where answer is incorrect
        else {
            mSudokuGridView.setIncorrectCell(potentialIndex);
            mSudokuGridView.setHighlightedCell(potentialIndex);
            mSoundPlayer.playWrongSound();
        }

        //Redraw grid
        mSudokuGridView.invalidate();
    }

    public void onClearCellPressed(View v) {
        int cellNumber = mSudokuGridView.getHighlightedCell();

        if (cellNumber == -1){
            //No cell is highlighted
            mSoundPlayer.playEmptyButtonSound();
        } else {
            mSudokuViewModel.setCellValue(cellNumber, 0);
            mSudokuGridView.clearHighlightedCell();
            mSudokuGridView.clearIncorrectCell();
            mSoundPlayer.playClearCellSound();
            mSudokuGridView.invalidate();
        }
    }

    /**
     * Sets up the 9 input buttons
     */
    private void initButtons() {

        //Initializing buttons
        mInputButtons = new Button[9];
        for(int i = 0; i < mInputButtons.length; i++)
        {
            //Sets the button array at index to have id button + the current index number
            //One is added because the number 0 is skipped
            mInputButtons[i] = findViewById(getResources().getIdentifier("button" + (i+1), "id",
                    this.getPackageName()));

            //Gets and sets the string the button should display
//            String buttonText = mVocabGame.getButtonString(i + 1);
          //  mInputButtons[i].setText(i + 1);

            //Links the listener to the button
            mInputButtons[i].setOnClickListener(onButtonClickListener);
        }
    }

    private void setButtonLabels(List<String> buttonLabels) {
        for(int i = 0; i < mInputButtons.length; i++) {
            mInputButtons[i].setText(buttonLabels.get(i));
        }
    }
}
