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

    private final String PUZZLE_KEY = "puzzle";
    private final String FOREIGN_WORDS_KEY = "foreign";
    private final String NATIVE_WORDS_KEY = "native";

    private VocabSudokuModel mVocabGame;
    private SudokuGridView mSudokuGridView;
    private LinearLayout mEditLayout;
    private TextView mTextInputView;
    private Button mClearButton;
    private Button mEnterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        if (savedInstanceState != null) {
            //Unpacking information from saved instance
            mVocabGame = new VocabSudokuModel(
                    savedInstanceState.getStringArray(NATIVE_WORDS_KEY),
                    savedInstanceState.getStringArray(FOREIGN_WORDS_KEY),
                    savedInstanceState.getIntArray(PUZZLE_KEY)
            );
        } else {
            //Unpacking information from intent
            Intent i = getIntent();
            mVocabGame = new VocabSudokuModel(
                    i.getStringArrayExtra(NATIVE_WORDS_KEY),
                    i.getStringArrayExtra(FOREIGN_WORDS_KEY),
                    i.getIntArrayExtra(PUZZLE_KEY)
            );
        }

        //Initializing sudoku grid
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

        updateAllViewLabels();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray(NATIVE_WORDS_KEY, mVocabGame.getAllNativeWords());
        outState.putStringArray(FOREIGN_WORDS_KEY, mVocabGame.getAllForeignWords());
        outState.putIntArray(PUZZLE_KEY, mVocabGame.getAllCellValues());
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
