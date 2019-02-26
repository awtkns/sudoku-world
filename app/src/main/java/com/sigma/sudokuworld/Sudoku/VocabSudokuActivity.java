package com.sigma.sudokuworld.Sudoku;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class VocabSudokuActivity extends SudokuActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.mSudokuGridView.setOnLongClickListener(onLongClickListener);
    }

    View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (mVocabGame.isLockedCell(cellTouched)) {
                String text = mVocabGame.getCellString(cellTouched, false);
                Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        }
    };
}
