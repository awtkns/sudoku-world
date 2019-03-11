package com.sigma.sudokuworld.sudoku;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.sigma.sudokuworld.game.GameMode;

public class VocabSudokuActivity extends SudokuActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mSudokuViewModel.getGameMode() != GameMode.NUMBERS) {
            super.mSudokuGridView.setOnLongClickListener(onLongClickListener);
        }
    }

    View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (mSudokuViewModel.isLockedCell(cellTouched)) {
                String text = mSudokuViewModel.getMappedString(
                        mSudokuViewModel.getCellValue(cellTouched),
                        GameMode.opposite(mSudokuViewModel.getGameMode())
                );
                Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        }
    };
}
