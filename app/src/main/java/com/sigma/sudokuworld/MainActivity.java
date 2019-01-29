package com.sigma.sudokuworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    GameModel mGameModel;
    SudokuGridView mSudokuGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGameModel = new GameModel(2);

        mSudokuGridView = findViewById(R.id.sudokugrid_view);
        mSudokuGridView.setOnTouchListener(onSudokuGridTouchListener);
        mSudokuGridView.setSudokuRootSize(mGameModel.SUDOKU_ROOT_SIZE);
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

                    if (mSudokuGridView.getCellLabel(cellNum).equals("")) {
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
}
