package com.sigma.sudokuworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;


public class SudokuActivity extends AppCompatActivity {

    GameModel mGameModel;
    SudokuGridView mSudokuGridView;
    String[] mNativeWords;
    String[] mForeignWords;
    int[] vals = {3, 0, 4, 0,
                  0, 2, 0, 0,
                  2, 0, 3, 0,
                  0, 0, 0, 4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        Intent i = getIntent();
        mNativeWords = i.getStringArrayExtra("native");
        mForeignWords = i.getStringArrayExtra("foreign");

        mGameModel = new GameModel(2, vals);

        mSudokuGridView = findViewById(R.id.sudokugrid_view);
        mSudokuGridView.setOnTouchListener(onSudokuGridTouchListener);
        mSudokuGridView.setSudokuRootSize(mGameModel.SUDOKU_ROOT_SIZE);
        setInitialNumbers();
    }

    SudokuGridView.OnTouchListener onSudokuGridTouchListener = new SudokuGridView.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            boolean wasEventHandled = false;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                if (mSudokuGridView.getGridBounds().contains(x, y)) {
                    int cellNum = mSudokuGridView.getCellNumberFromCoordinates(x, y);

                    if (mGameModel.isLockedCell(cellNum)) {

                    }
                    else if (mSudokuGridView.getCellLabel(cellNum).equals("")) {
                        mSudokuGridView.setCellLabel(cellNum, "1");
                    } else mSudokuGridView.setCellLabel(cellNum, "");

                    mSudokuGridView.invalidate();
                    mSudokuGridView.performClick();
                    wasEventHandled = true;
                }
            }

            return wasEventHandled;
        }
    };

    private void setInitialNumbers() {
        for (int i = 0; i < mGameModel.SUDOKU_NUMBER_OF_CELLS; i++) {
            int val = mGameModel.getCellValue(i);

            if (val != 0) {
                mSudokuGridView.setCellLabel(i, mNativeWords[val - 1]);
            }
        }
    }
}
