package com.sigma.sudokuworld.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import com.sigma.sudokuworld.game.GameMode;
import com.sigma.sudokuworld.persistence.GameRepository;
import com.sigma.sudokuworld.persistence.WordSetRepository;
import com.sigma.sudokuworld.persistence.db.entities.Game;
import com.sigma.sudokuworld.persistence.db.entities.Word;
import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;

import java.util.ArrayList;
import java.util.List;

public class SudokuViewModel extends SettingsViewModel {
    private final int SUDOKU_SIZE;
    private GameRepository mGameRepository;
    private Game mGame;

    private MutableLiveData<List<String>> cellLabelsLiveData;
    private MutableLiveData<List<String>> buttonLabelsLiveData;

    private List<String> labels;
    private List<String> buttonLabels;
    private Word[] foreignWords;
    private Word[] nativeWords;
    private String[] nWords;
    private String[] fWords;

    private SparseArray<String> nativeWordsMap;
    private SparseArray<String> foreignWordsMap;

    private String TAG = "SudokuViewModel";

    //Constructor loads a saved game
    SudokuViewModel(@NonNull Application application, int saveID) {
        super(application);
        mGameRepository = new GameRepository(application);

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

    public LiveData<List<String>> getButtonLabels() {
        return buttonLabelsLiveData;
    }

    public String getMappedString(int value, GameMode mode) {
        return valueToMappedLabel(value, mode);
    }

    public int getCellValue(int cellNumber) {
        return mGame.getCellValue(cellNumber);
    }

    public void setCellValue(int cellNumber, int value) {
        if (cellNumber > mGame.getCellValues().length || cellNumber < 0) {
            Log.wtf(TAG, "Invalid Cell number");
            return;
        }

        // Locked cell
        if (mGame.getLockedCells()[cellNumber]) return;


        mGame.setCellValue(cellNumber, value);
        updateCellLabel(cellNumber, value);
    }

    public GameMode getGameMode() {
        return mGame.getGameMode();
    }

    public boolean isLockedCell(int cellNumber) {
        return mGame.isLocked(cellNumber);
    }

    public boolean isCorrectValue(int cellNumber, int value) {
        return mGame.getSolutionValue(cellNumber) ==  value;
    }

    public boolean isCellCorrect(int cellNumber) {
        return mGame.getSolutionValue(cellNumber) ==  mGame.getCellValue(cellNumber);
    }

    public int getIncorrectCellNumber() {
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            if (mGame.getCellValue(i) != mGame.getSolutionValue(i)) return i;
        }

        //Returns -1 if game is solved;
        return -1;
    }

    private void updateCellLabel(int cellNumber, int value) {
        GameMode gameMode = mGame.getGameMode();
        labels.set(cellNumber, valueToMappedLabel(value, gameMode));
        cellLabelsLiveData.setValue(labels); //TODO: Don't run on main thread
    }

    private void init() {
        initializeWordMaps();
        initCellLabelsLiveData();
        initButtonLabelsLiveData();
    }

    private void initCellLabelsLiveData() {
        cellLabelsLiveData = new MutableLiveData<>();
        labels = new ArrayList<>();

        GameMode gameMode = mGame.getGameMode();
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            String label = "";
            if (mGame.isLocked(i)) {
                label += KeyConstants.CELL_LOCKED_FLAG;
                label += valueToMappedLabel(mGame.getCellValue(i), GameMode.opposite(gameMode));
            } else {
                label += valueToMappedLabel(mGame.getCellValue(i), gameMode);
            }

            labels.add(i, label);
        }

        cellLabelsLiveData.setValue(labels); //TODO: Don't run on main thread
    }

    private void initButtonLabelsLiveData() {
        buttonLabelsLiveData = new MutableLiveData<>();
        buttonLabels = new ArrayList<>();

        GameMode gameMode = mGame.getGameMode();
        gameMode = GameMode.opposite(gameMode);

        for (int i = 0; i < 9; i++) {
            String label = "";
            label += valueToMappedLabel(i + 1, GameMode.opposite(gameMode));

            buttonLabels.add(i, label);
        }

        buttonLabelsLiveData.setValue(buttonLabels); //TODO: Don't run on main thread
    }

    private void initializeWordMaps() {
        WordSetRepository wordSetRepository = new WordSetRepository(getApplication());
        //foreignWords = wordSetRepository.getForeignWordsInSet(mGame.getSetID());        //TODO: SET BUILDER
        //nativeWords = wordSetRepository.getNativeWordsInSet(mGame.getSetID());
        nWords = new String[] {
                "Red",
                "Pink",
                "Green",
                "Purple",
                "Yellow",
                "White",
                "Black",
                "Brown",
                "Blue"};
        fWords = new String[] {
                "Rouge",
                "Rose",
                "Vert",
                "Violet",
                "Jaune",
                "Blanc",
                "Noir",
                "Marron",
                "Bleu"
        };

        nativeWordsMap = new SparseArray<>();
        nativeWordsMap.append(0, "");
//        for(int i = 0; i < nativeWords.length; i++) {
//            nativeWordsMap.append(i + 1, nativeWords[i].getWord());
//        }
        for(int i = 0; i < nWords.length; i++) {
            nativeWordsMap.append(i + 1, nWords[i]);
        }

        foreignWordsMap = new SparseArray<>();
        foreignWordsMap.append(0, "");
//        for(int i = 0; i < foreignWords.length; i++) {
//            foreignWordsMap.append(i + 1, foreignWords[i].getWord());
//        }
        for(int i = 0; i < fWords.length; i++) {
            foreignWordsMap.append(i + 1, fWords[i]);
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

