package com.sigma.sudokuworld.game;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sigma.sudokuworld.persistence.GameRepository;
import com.sigma.sudokuworld.persistence.db.entities.GameSave;

public class SudokuViewModel extends AndroidViewModel {
    private GameRepository mGameRepository;
    private GameSave mGame;

    //Constructor that creates a new game
    public SudokuViewModel(@NonNull Application application, GameDifficulty difficulty) {
        super(application);
        mGameRepository = new GameRepository(application);

        SudokuRobot robot = new SudokuRobot(3, difficulty);
//        String cellValues = SudokuRobot.

    }

    //Default Constructor
    public SudokuViewModel(@NonNull Application application, int saveID) {
        super(application);

    }


    private String intArrayToSaveFormat(int[] array) {
        String string = "";
        for (int i : array) {

        }
        return string;
    }
}
