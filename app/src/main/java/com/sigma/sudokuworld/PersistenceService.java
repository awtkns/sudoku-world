package com.sigma.sudokuworld;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Map;

import static com.sigma.sudokuworld.KeyConstants.*;


public abstract class PersistenceService  {

    //Private file names for what xml file to write to
    private static final String SAVE_GAME_FILE = "save";
    private static final String SAVE_SETTINGS_FILE = "settings";

    //Private index keys for read / writing arrays to xml
    private static final String VAlUES_KEY_PREFIX = "valueCell";
    private static final String SOLUTION_KEY_PREFIX = "solutionCell";
    private static final String LOCKED_KEY_PREFIX = "lockedCell";
    private static final String NATIVE_KEY_PREFIX = "nativeWord";
    private static final String FOREIGN_KEY_PREFIX = "foreignWord";



    static void saveGameData(Context context, Bundle data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SAVE_GAME_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Unpacking "save" bundle
        GameDifficulty difficulty = (GameDifficulty) data.getSerializable(DIFFICULTY_KEY);
        GameMode mode = (GameMode) data.getSerializable(MODE_KEY);
        int arraySize = data.getInt(SUDOKU_SIZE_KEY);
        int[] cellValues = data.getIntArray(CELL_VALUES_KEY);
        int[] solutionValues = data.getIntArray(SOLUTION_VALUES_KEY);
        boolean[] lockedCells = data.getBooleanArray(LOCKED_CELLS_KEY);
        String[] nativeWords = data.getStringArray(NATIVE_WORDS_KEY);
        String[] foreignWords = data.getStringArray(FOREIGN_WORDS_KEY);

        editor.putString(DIFFICULTY_KEY, difficulty.name());
        editor.putString(MODE_KEY, mode.name());
        editor.putInt(SUDOKU_SIZE_KEY, arraySize);

        //Writing cell data
        for (int i=0; i < arraySize; i++) {
            String index = Integer.toString(i);
            editor.putInt(VAlUES_KEY_PREFIX + index, cellValues[i]);
            editor.putInt(SOLUTION_KEY_PREFIX + index, solutionValues[i]);
            editor.putBoolean(LOCKED_KEY_PREFIX + index, lockedCells[i]);
        }

        //Writing word list
        for (int i=0; i < nativeWords.length; i++) {
            String index = Integer.toString(i);
            editor.putString(NATIVE_KEY_PREFIX + index, nativeWords[i]);
            editor.putString(FOREIGN_KEY_PREFIX + index, foreignWords[i]);
        }

        //Make changes to xml file
        editor.apply();
    }

    static Bundle loadGameData(Context context) throws NullPointerException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SAVE_GAME_FILE, Context.MODE_PRIVATE);
        Bundle data = new Bundle();

        Map<String, ?> dataMap = sharedPreferences.getAll();
        int size = (Integer) dataMap.get(SUDOKU_SIZE_KEY);
        data.putInt(SUDOKU_SIZE_KEY, size);

        return data;
    }
}
