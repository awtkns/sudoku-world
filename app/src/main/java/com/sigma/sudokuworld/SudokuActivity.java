package com.sigma.sudokuworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SudokuActivity extends AppCompatActivity {

    VocabSudokuModel mVocabGame;
    SudokuGridView mSudokuGridView;
    LinearLayout mEditLayout;
    TextView mTextInputView;
    Button mClearButton;
    Button mEnterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        //Unpacking information from intent
        Intent i = getIntent();
        mVocabGame = new VocabSudokuModel(
                i.getStringArrayExtra("native"),
                i.getStringArrayExtra("foreign"),
                i.getIntArrayExtra("puzzle")
        );

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

    //When soduku grid is touched
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
                    if (mVocabGame.isLockedCell(cellNum)) {
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
        for (int i = 0; i < 81; i++) {
            mSudokuGridView.setCellLabel(i, SudokuGridView.LOCKED_FLAG + mVocabGame.getCellString(i));
        }

        mSudokuGridView.invalidate();
    }
}
