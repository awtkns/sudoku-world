package com.sigma.sudokuworld;

import android.content.Intent;
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
    private final String GAME_MODE_INTENT_KEY = "gameMode";


    VocabSudokuModel mVocabGame;
    GameMode mGameMode;
    SudokuGridView mSudokuGridView;
    LinearLayout mEditLayout;
    TextView mTextInputView;
    Button mClearButton;
    Button mEnterButton;
    Button mCheckAnswerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        if (savedInstanceState != null) {
            //Unpacking information from saved instance
            mGameMode =(GameMode) savedInstanceState.getSerializable(GAME_MODE_INTENT_KEY);
            mVocabGame = new VocabSudokuModel(
                    savedInstanceState.getStringArray(NATIVE_WORDS_INTENT_KEY),
                    savedInstanceState.getStringArray(FOREIGN_WORDS_INTENT_KEY),
                    savedInstanceState.getIntArray(PUZZLE_INTENT_KEY),
                    savedInstanceState.getIntArray(SOLUTION_INTENT_KEY),
                    savedInstanceState.getBooleanArray(INITIAL_CELLS_INTENT_KEY),
                    mGameMode
            );
        } else {
            //Unpacking information from intent
            Intent i = getIntent();
            mGameMode = (GameMode) i.getSerializableExtra(GAME_MODE_INTENT_KEY);
            mVocabGame = new VocabSudokuModel(
                    i.getStringArrayExtra(NATIVE_WORDS_INTENT_KEY),
                    i.getStringArrayExtra(FOREIGN_WORDS_INTENT_KEY),
                    mGameMode
            );
        }


        //Initializing Sudoku grid
        mSudokuGridView = findViewById(R.id.sudokugrid_view);
        mSudokuGridView.setOnTouchListener(onSudokuGridTouchListener);

        mEditLayout = findViewById(R.id.inputLayout);
        mEditLayout.setVisibility(View.GONE);

        mTextInputView = findViewById(R.id.textInputView);

        mClearButton = findViewById(R.id.clearButton);
        mClearButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mClearButton.setOnClickListener(onClearButtonClickLister);

        mEnterButton = findViewById(R.id.enterButton);
        mEnterButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        mCheckAnswerButton = findViewById(R.id.checkAnswerButton);
        mCheckAnswerButton.setOnClickListener(onCheckAnswerButtonClickLister);

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
        outState.putSerializable(GAME_MODE_INTENT_KEY, mGameMode);
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
                        mEditLayout.setVisibility(View.GONE);           //Hide word input
                    } else {
                        mEditLayout.setVisibility(View.VISIBLE);        //Show word input
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

    View.OnClickListener onClearButtonClickLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSudokuGridView.clearHighlightedCell();
            mEditLayout.setVisibility(View.GONE);
            mSudokuGridView.invalidate();
        }
    };

    View.OnClickListener onCheckAnswerButtonClickLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Checks if the answers are right and displays the first wrong cell (if any)
            int potentialIndex = mVocabGame.checkGame();
            //Clear highlights / what cell is selected for input
            mEditLayout.setVisibility(View.GONE);
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
