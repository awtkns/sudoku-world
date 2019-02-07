package com.sigma.sudokuworld;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public abstract class PersistenceService  {

    private static final String GAME_DATA_SAVE = "gameSave";
    private static final String VAlUES_KEY_PREFIX = "valueCell";
    private static final String SOLUTION_KEY_PREFIX = "solutionCell";
    static final String SUDOKU_SIZE_KEY = "size";
    static final String CELL_VALUES_KEY = "values";

    public static void saveGameData(Context context, Bundle data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GAME_DATA_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int arraySize = data.getInt(SUDOKU_SIZE_KEY);
        int[] cellValues = data.getIntArray(CELL_VALUES_KEY);

        editor.putInt(SUDOKU_SIZE_KEY, arraySize);
        for (int i=0; i < arraySize; i++) {

            String key = VAlUES_KEY_PREFIX + Integer.toString(i);
            editor.putInt(key, cellValues[i]);
        }

        editor.apply();
    }

    public static Bundle loadGameData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(GAME_DATA_SAVE, Context.MODE_PRIVATE);
        Bundle data = new Bundle();

        data.getInt(SUDOKU_SIZE_KEY);
        return data;
    }
}
