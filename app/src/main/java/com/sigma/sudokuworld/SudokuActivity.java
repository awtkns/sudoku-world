package com.sigma.sudokuworld;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SudokuActivity extends AppCompatActivity {

    private final String PUZZLE_INTENT_KEY = "puzzle";
    private final String SOLUTION_INTENT_KEY = "solution";
    private final String INITIAL_CELLS_INTENT_KEY = "initial";
    private final String FOREIGN_WORDS_INTENT_KEY = "foreign";
    private final String NATIVE_WORDS_INTENT_KEY = "native";
    private final String GAME_MODE_INTENT_KEY = "mode";
    private final String GAME_DIFFICULTY_INTENT_KEY = "difficulty";


    VocabSudokuModel mVocabGame;
    SudokuGridView mSudokuGridView;
    Button[] sudokuButtons;
    Button mClearCellButton;
    Button mCheckAnswerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        if (savedInstanceState != null) {
            //Unpacking information from saved instance
            mVocabGame = new VocabSudokuModel(
                    savedInstanceState.getStringArray(NATIVE_WORDS_INTENT_KEY),
                    savedInstanceState.getStringArray(FOREIGN_WORDS_INTENT_KEY),
                    savedInstanceState.getIntArray(PUZZLE_INTENT_KEY),
                    savedInstanceState.getIntArray(SOLUTION_INTENT_KEY),
                    savedInstanceState.getBooleanArray(INITIAL_CELLS_INTENT_KEY),
                    (GameMode) savedInstanceState.getSerializable(GAME_MODE_INTENT_KEY)
            );
        } else {
            //Unpacking information from intent
            Intent i = getIntent();
            mVocabGame = new VocabSudokuModel(
                    i.getStringArrayExtra(NATIVE_WORDS_INTENT_KEY),
                    i.getStringArrayExtra(FOREIGN_WORDS_INTENT_KEY),
                    (GameMode) i.getSerializableExtra(GAME_MODE_INTENT_KEY),
                    (GameDifficulty) i.getSerializableExtra(GAME_DIFFICULTY_INTENT_KEY)
            );
        }


        //Initializing Sudoku grid
        mSudokuGridView = findViewById(R.id.sudokugrid_view);
        mSudokuGridView.setOnTouchListener(onSudokuGridTouchListener);


        //Initializing buttons
        sudokuButtons = new Button[9];
        for(int buttonNumber = 0; buttonNumber < 9; buttonNumber++)
        {
            //Sets the button array at index to have id button + the current index number
            //One is added because the number 0 is skipped
            sudokuButtons[buttonNumber] = findViewById(getResources().getIdentifier("button" + (buttonNumber+1), "id",
                    this.getPackageName()));

            //Gets and sets the string the button should display
            String buttonText = mVocabGame.getButtonString(buttonNumber + 1);
            sudokuButtons[buttonNumber].setText(buttonText);

            //Links the listener to the button
            sudokuButtons[buttonNumber].setOnClickListener(onButtonClickListener);
        }

        mClearCellButton = findViewById(R.id.clearCellButton);
        mClearCellButton.setOnClickListener(onButtonClickListener);

        mCheckAnswerButton = findViewById(R.id.checkAnswerButton);
        mCheckAnswerButton.setOnClickListener(onCheckAnswerButtonClickListener);

        updateAllViewLabels();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the current state of the Sudoku board
        outState.putStringArray(NATIVE_WORDS_INTENT_KEY, mVocabGame.getAllNativeWords());
        outState.putStringArray(FOREIGN_WORDS_INTENT_KEY, mVocabGame.getAllForeignWords());
        outState.putIntArray(PUZZLE_INTENT_KEY, mVocabGame.getAllCellValues());
        outState.putIntArray(SOLUTION_INTENT_KEY, mVocabGame.getSolutionValues());
        outState.putBooleanArray(INITIAL_CELLS_INTENT_KEY, mVocabGame.getAllIntialCells());
        outState.putSerializable(GAME_MODE_INTENT_KEY, mVocabGame.getGameMode());
    }

    @Override
    protected void onPause() {
        super.onPause();

        Bundle saveData = new Bundle();
        saveData.putInt(PersistenceService.SUDOKU_SIZE_KEY, 81);
        saveData.putIntArray(PersistenceService.CELL_VALUES_KEY, mVocabGame.getAllCellValues());
        PersistenceService.saveGameData(this, saveData);
    }

    //When sudoku grid is touched
    SudokuGridView.OnTouchListener onSudokuGridTouchListener = new SudokuGridView.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            boolean wasEventHandled = false;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                //If touch in the bound of the grid
                if (mSudokuGridView.getGridBounds().contains(x, y)) {

                    //Clear previous highlighted cell
                    mSudokuGridView.clearHighlightedCell();

                    //Cell that was touched
                    int cellNum = mSudokuGridView.getCellNumberFromCoordinates(x, y);

                    //If we have selected the incorrect cell, un highlight it
                    if (cellNum == mSudokuGridView.getIncorrectCell())
                    {mSudokuGridView.clearIncorrectCell(); }

                    //The the cell is locked (ei: not one where you can change the number)
                    if (mVocabGame.isInitialCell(cellNum)) {
                    } else {
                        mSudokuGridView.setHighlightedCell(cellNum);    //Set new highlighted cell
                    }

                    //Force redraw view
                    mSudokuGridView.invalidate();
                    mSudokuGridView.performClick();
                    wasEventHandled = true;
                }
            }

            return wasEventHandled;
        }
    };

    View.OnClickListener onButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            int buttonValue = 0;

            //Loop through all our possible buttons to see which button is clicked
            //Set buttonValue to the corresponding button
            //If no button is found in for loop, clear button is being called so buttonValue = 0
            for (int buttonindex = 0; buttonindex < 9; buttonindex++) {
                if (button == sudokuButtons[buttonindex]){
                    buttonValue = buttonindex + 1;
                }
            }
            int cellNumber = mSudokuGridView.getHighlightedCell();
            if (cellNumber == -1){ return; }

            mVocabGame.setCellString(cellNumber, buttonValue);
            mSudokuGridView.setCellLabel(cellNumber, mVocabGame.getButtonString(buttonValue));

            //Clears selected cells and redraws
            mSudokuGridView.clearHighlightedCell();
            mSudokuGridView.clearIncorrectCell();
            mSudokuGridView.invalidate();
        }
    };


    View.OnClickListener onCheckAnswerButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Check if cell is selected
            //If a cell is selected, check if that cell is correct
            int highlightedCell = mSudokuGridView.getHighlightedCell();
            if (highlightedCell != -1){
                if (mVocabGame.isCellCorrect(highlightedCell)){
                    int i =0;
                    mSudokuGridView.clearHighlightedCell();
                    mSudokuGridView.invalidate();
                    return;
                }
                mSudokuGridView.setIncorrectCell(highlightedCell);
                mSudokuGridView.invalidate();
                return;
            }



            //Checks if the answers are right and displays the first wrong cell (if any)
            int potentialIndex = mVocabGame.checkGame();
            //Clear highlights / what cell is selected for input
            mSudokuGridView.clearHighlightedCell();

            //Case where answer is correct
            if (potentialIndex == -1) {
                int i = 0;
            }

            //Case where answer is incorrect
            else {
                mSudokuGridView.setIncorrectCell(potentialIndex);
                mSudokuGridView.setHighlightedCell(potentialIndex);
            }

            //Redraw grid
            mSudokuGridView.invalidate();
        }
    };

    private void updateAllViewLabels() {
        for (int cellNumber = 0; cellNumber < 81; cellNumber++) {
            String label = mVocabGame.getCellString(cellNumber);

            if (mVocabGame.isInitialCell(cellNumber)) {
                label = SudokuGridView.LOCKED_FLAG + label;
            }

            mSudokuGridView.setCellLabel(cellNumber, label);
        }

        mSudokuGridView.invalidate();
    }
}
