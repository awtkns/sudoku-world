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
    private MutableLiveData<List<String>> buttonLabelsLiveData;

    private List<String> labels;
    private Word[] foreignWords;
    private Word[] nativeWords;
    private SparseArray<String> nativeWordsMap;
    private SparseArray<String> foreignWordsMap;

    private String TAG = "SudokuViewModel";

    //Constructor loads a saved game
    public SudokuViewModel(@NonNull Application application, int saveID) {
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

    public LiveData<List<String>> getButtonLabel() {
        return buttonLabelsLiveData;
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
            String label = "";
            if (mGame.isLocked(i)) label = CELL_LABEL_LOCKED_FLAG;
            label += valueToMappedLabel(mGame.getCellValue(i), gameMode);

            labels.add(i, label);
        }

        cellLabelsLiveData.setValue(labels); //TODO: Don't run on main thread
    }

//    private void intiButtonLabelsLiveData
//
    private void initializeWordMaps() {
        WordSetRepository wordSetRepository = new WordSetRepository(getApplication());
        //foreignWords = wordSetRepository.getForeignWordsInSet(mGame.getSetID());        //TODO: SET BUILDER
        //nativeWords = wordSetRepository.getNativeWordsInSet(mGame.getSetID());
        String[] nWords = new String[] {
                "Red",
                "Pink",
                "Green",
                "Purple",
                "Yellow",
                "White",
                "Black",
                "Brown",
                "Blue"};
        String[] fWords = new String[] {
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

