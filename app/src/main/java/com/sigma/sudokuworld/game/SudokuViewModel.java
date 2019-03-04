package com.sigma.sudokuworld.game;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.sigma.sudokuworld.persistence.GameRepository;
import com.sigma.sudokuworld.persistence.db.entities.Game;
import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;

import java.util.Objects;

public class SudokuViewModel extends AndroidViewModel {
    public static final String CELL_LABEL_LOCKED_FLAG = "~";

    private GameRepository mGameRepository;
    private Game mGame;

    private String[] mCellLabels;

    private String TAG = "SudokuViewModel";

    //Constructor that creates a new game
    public SudokuViewModel(@NonNull Application application, GameDifficulty difficulty, GameMode gameMode) {
        super(application);
        mGameRepository = new GameRepository(application);

        SudokuRobot robot = new SudokuRobot(3);
        Bundle puzzle = robot.generatePuzzle(difficulty);

        mGame = new Game(
                //SaveID 0 = auto generate
                0, 0,
                difficulty,
                gameMode,
                Objects.requireNonNull(puzzle.getIntArray(KeyConstants.CELL_VALUES_KEY)),
                Objects.requireNonNull(puzzle.getIntArray(KeyConstants.SOLUTION_VALUES_KEY)),
                Objects.requireNonNull(puzzle.getBooleanArray(KeyConstants.LOCKED_CELLS_KEY))
        );

        mGameRepository.newGame(mGame);
        initCellLabels();
    }

    //Constructor that loads a saved game
    public SudokuViewModel(@NonNull Application application, int saveID) {
        super(application);

        try {
            mGame = mGameRepository.getGameSaveByID(saveID);//
        } catch (NullPointerException e) {
            Log.wtf(TAG, "Tried loading game save that doesn't exit");
        }
        initCellLabels();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mGameRepository.saveGame(mGame);
    }

    public boolean setCell(int cellNumber, String value) {
        if (cellNumber > mCellLabels.length || cellNumber < 1) {
            Log.wtf(TAG, "Invalid Cell number");
            return false;
        }

        // Locked cell
        if (mGame.getLockedCells()[cellNumber]) return false;


        mCellLabels[cellNumber] = value;
        return true;
    }


    private void initCellLabels() {
        mCellLabels = new String[mGame.getCellValues().length];

        if (mGame.getGameMode() == GameMode.NUMBERS) {
            for (int i = 0; i < mCellLabels.length; i++) {
                mCellLabels[i] = "";

                int cellValue = mGame.getCellValue(i);
                if (cellValue != 0) {
                    if (mGame.isLocked(i)) mCellLabels[i] = CELL_LABEL_LOCKED_FLAG;
                    mCellLabels[i] += mGame.getCellValue(cellValue);
                }
            }
        }
    }
}
