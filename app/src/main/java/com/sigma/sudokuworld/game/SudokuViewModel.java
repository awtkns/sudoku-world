package com.sigma.sudokuworld.game;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import com.sigma.sudokuworld.persistence.GameRepository;
import com.sigma.sudokuworld.persistence.WordSetRepository;
import com.sigma.sudokuworld.persistence.db.entities.Game;
import com.sigma.sudokuworld.persistence.db.entities.Word;
import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SudokuViewModel extends AndroidViewModel {
    public static final String CELL_LABEL_LOCKED_FLAG = "~";

    private final int SUDOKU_SIZE;
    private GameRepository mGameRepository;
    private Game mGame;


    private MutableLiveData<List<String>> cellLabelsLiveData;
    private List<String> labels;
    private Word[] foreignWords;
    private Word[] nativeWords;
    private SparseArray<String> nativeWordsMap;
    private SparseArray<String> foreignWordsMap;

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
        SUDOKU_SIZE = mGame.getCellValues().length;
        init();
    }

    //Constructor that loads a saved game
    public SudokuViewModel(@NonNull Application application, int saveID) {
        super(application);


        try {
            mGame = mGameRepository.getGameSaveByID(saveID);//
        } catch (NullPointerException e) {
            Log.wtf(TAG, "Tried loading game save that doesn't exit");
        }

        SUDOKU_SIZE = mGame.getCellValues().length;
        init();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mGameRepository.saveGame(mGame);
    }

    public LiveData<List<String>> getCellLabels() {
        return cellLabelsLiveData;
    }

    public boolean setCellValue(int cellNumber, int value) {
        if (cellNumber > mGame.getCellValues().length || cellNumber < 1) {
            Log.wtf(TAG, "Invalid Cell number");
            return false;
        }

        // Locked cell
        if (mGame.getLockedCells()[cellNumber]) return false;


        mGame.setCellValue(cellNumber, value);
        updateCellLabel(cellNumber, value);
        return true;
    }

    private void updateCellLabel(int cellNumber, int value) {
        GameMode gameMode = mGame.getGameMode();
        labels.set(cellNumber, valueToMappedLabel(cellNumber, gameMode));
        cellLabelsLiveData.setValue(labels); //TODO: Don't run on main thread
    }

    private void init() {
        initializeWordMaps();
        initCellLabelsLiveData();
    }

    private void initCellLabelsLiveData() {
        cellLabelsLiveData = new MutableLiveData<>();
        labels = new ArrayList<>();

        GameMode gameMode = mGame.getGameMode();
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            labels.add(i, valueToMappedLabel(mGame.getCellValue(i), gameMode));
        }

        cellLabelsLiveData.setValue(labels); //TODO: Don't run on main thread
    }

    private void initializeWordMaps() {
        WordSetRepository wordSetRepository = new WordSetRepository(getApplication());
        foreignWords = wordSetRepository.getForeignWordsInSet(mGame.getSetID());
        nativeWords = wordSetRepository.getNativeWordsInSet(mGame.getSetID());

        nativeWordsMap = new SparseArray<>();
        nativeWordsMap.append(0, "");
        for(int i = 0; i < nativeWords.length; i++) {
            nativeWordsMap.append(i + 1, nativeWords[i].getWord());
        }

        foreignWordsMap = new SparseArray<>();
        foreignWordsMap.append(0, "");
        for(int i = 0; i < foreignWords.length; i++) {
            foreignWordsMap.append(i + 1, foreignWords[i].getWord());
        }
    }

    private String valueToMappedLabel(int value, GameMode gameMode) {
        String label = "";

        if (value != 0) {
            if (gameMode == GameMode.NUMBERS) label = Integer.toString(value);
            else if (gameMode == GameMode.NATIVE) label = nativeWordsMap.valueAt(value);
            else if (gameMode == GameMode.FOREIGN) label = foreignWordsMap.valueAt(value);
        }

        return label;
    }

}

