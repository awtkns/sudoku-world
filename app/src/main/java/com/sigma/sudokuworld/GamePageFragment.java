package com.sigma.sudokuworld;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;
import com.sigma.sudokuworld.sudoku.SudokuGridView;

public class GamePageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game_page, container, false);
        SudokuGridView sudokuGridView = view.findViewById(R.id.sudokuGridViewPage);
        TextView difficulty = view.findViewById(R.id.pageDifficulty);
        TextView mode = view.findViewById(R.id.pageMode);

        try {
            Bundle args = getArguments();
            boolean[] lockedCells = args.getBooleanArray(KeyConstants.LOCKED_CELLS_KEY);
            sudokuGridView.lazySetLockedCellsLabels(lockedCells);
            difficulty.setText(args.getString(KeyConstants.DIFFICULTY_KEY));
            mode.setText(args.getString(KeyConstants.MODE_KEY));
        } catch (NullPointerException e) {
            Log.d("Pager", "onCreateView: no filler data");
        }

        return view;
    }
}

