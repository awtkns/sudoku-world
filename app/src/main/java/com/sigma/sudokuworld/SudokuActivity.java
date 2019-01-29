package com.sigma.sudokuworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class SudokuActivity extends AppCompatActivity {

    GameModel mGameModel;
    SudokuGridView mSudokuGridView;
    String[] mNativeWords;
    String[] mForeignWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        Intent i = getIntent();
        mNativeWords = i.getStringArrayExtra("native");
        mForeignWords = i.getStringArrayExtra("foreign");

        mGameModel = new GameModel(3, i.getIntArrayExtra("puzzle"));

        mSudokuGridView = findViewById(R.id.sudokugrid_view);
        mSudokuGridView.setOnTouchListener(onSudokuGridTouchListener);
        mSudokuGridView.setSudokuRootSize(mGameModel.SUDOKU_ROOT_SIZE);

        updateAllViewLabels();
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
                        //Locked cell
                    } else if (mSudokuGridView.getCellLabel(cellNum).equals("")) {
                        mSudokuGridView.setCellLabel(cellNum, "f");
                    } else mSudokuGridView.setCellLabel(cellNum, "");

                    mSudokuGridView.invalidate();
                    mSudokuGridView.performClick();
                    wasEventHandled = true;
                }
            }

            return wasEventHandled;
        }
    };

    private void updateAllViewLabels() {
        for (int i = 0; i < mGameModel.SUDOKU_NUMBER_OF_CELLS; i++) {
            int val = mGameModel.getCellValue(i);

            if (val != 0) {
                try {
                    mSudokuGridView.setCellLabel(i, SudokuGridView.LOCKED_FLAG + mNativeWords[val - 1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.w("SudokuActivity", "Attempting to mapped val to no word");
                    mSudokuGridView.setCellLabel(i, Integer.toString(mGameModel.getCellValue(i)));
                }
            }
        }

        mSudokuGridView.invalidate();
    }
}
