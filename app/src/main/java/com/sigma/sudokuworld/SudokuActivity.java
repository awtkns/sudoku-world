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

    GameModel mGameModel;
    SudokuGridView mSudokuGridView;
    LinearLayout mEditLayout;
    TextView mTextInputView;
    Button mClearButton;
    Button mEnterButton;


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
                        mEditLayout.setVisibility(View.GONE);
                        mSudokuGridView.clearHighlightedCell();
                    } else {
                        mEditLayout.setVisibility(View.VISIBLE);
                        mSudokuGridView.setHighlightedCell(cellNum);
                    }

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
