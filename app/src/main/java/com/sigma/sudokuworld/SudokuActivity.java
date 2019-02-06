package com.sigma.sudokuworld;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SudokuActivity extends AppCompatActivity {

    private final String PUZZLE_INTENT_KEY = "puzzle";
    private final String SOLUTION_INTENT_KEY = "solution";
    private final String INITIAL_CELLS_INTENT_KEY = "initial";
    private final String FOREIGN_WORDS_INTENT_KEY = "foreign";
    private final String NATIVE_WORDS_INTENT_KEY = "native";
    private final String GAME_MODE_INTENT_KEY = "mode";


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
                    (GameMode) i.getSerializableExtra(GAME_MODE_INTENT_KEY)
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
            String buttonText = mVocabGame.getMapValue(buttonNumber + 1);
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
        outState.putStringArray(NATIVE_WORDS_INTENT_KEY, mVocabGame.getAllNativeWords());
        outState.putStringArray(FOREIGN_WORDS_INTENT_KEY, mVocabGame.getAllForeignWords());
        outState.putIntArray(PUZZLE_INTENT_KEY, mVocabGame.getAllCellValues());
        outState.putIntArray(SOLUTION_INTENT_KEY, mVocabGame.getSolutionValues());
        outState.putBooleanArray(INITIAL_CELLS_INTENT_KEY, mVocabGame.getAllIntialCells());
        outState.putSerializable(GAME_MODE_INTENT_KEY, mVocabGame.getGameMode());
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
            int buttonValue = 0;
            switch(v.getId())
            {
                case R.id.button1:
                    buttonValue = 1;
                    break;
                case R.id.button2:
                    buttonValue = 2;
                    break;
                case R.id.button3:
                    buttonValue =3;
                    break;
                case R.id.button4:
                    buttonValue =4;
                    break;
                case R.id.button5:
                    buttonValue =5;
                    break;
                case R.id.button6:
                    buttonValue =6;
                    break;
                case R.id.button7:
                    buttonValue =7;
                    break;
                case R.id.button8:
                    buttonValue =8;
                    break;
                case R.id.button9:
                    buttonValue =9 ;
                    break;
                case R.id.clearCellButton:
                    buttonValue = 0;
                    break;

            }
            int cellNumber = mSudokuGridView.getHighlightedCell();
            if (cellNumber == -1){ return; }

            mVocabGame.setCellString(cellNumber, buttonValue);
            mSudokuGridView.setCellLabel(cellNumber, mVocabGame.getCellString(cellNumber));
            mSudokuGridView.invalidate();
        }
    };


    View.OnClickListener onCheckAnswerButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
