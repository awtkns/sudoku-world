package com.sigma.sudokuworld.persistence;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.game.GameDifficulty;
import com.sigma.sudokuworld.game.GameMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

import static com.sigma.sudokuworld.persistence.KeyConstants.*;


public abstract class PersistenceService {

    private static final String TAG = "PERSISTENCE_SERVICE";


    //Private file names for what xml file to write to
    private static final String SAVE_GAME_FILE = "save";
    private static final String SAVE_SETTINGS_FILE = "settings";

    //Private array size keys
    private static final String WORD_LIST_SIZE = "word_size";
    private static final String SUDOKU_SIZE_KEY = "sudoku_size";

    //Private index keys for read / writing arrays to xml
    private static final String VAlUES_KEY_PREFIX = "valueCell";
    private static final String SOLUTION_KEY_PREFIX = "solutionCell";
    private static final String LOCKED_KEY_PREFIX = "lockedCell";
    private static final String NATIVE_KEY_PREFIX = "nativeWord";
    private static final String FOREIGN_KEY_PREFIX = "foreignWord";

    /**
     * Saves game data
     * @param context caller
     * @param data bundle
     */
    public static void saveGameData(Context context, Bundle data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SAVE_GAME_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Unpacking "save" bundle
        GameDifficulty difficulty = (GameDifficulty) data.getSerializable(DIFFICULTY_KEY);
        GameMode mode = (GameMode) data.getSerializable(MODE_KEY);
        int[] cellValues = data.getIntArray(CELL_VALUES_KEY);
        int[] solutionValues = data.getIntArray(SOLUTION_VALUES_KEY);
        boolean[] lockedCells = data.getBooleanArray(LOCKED_CELLS_KEY);
        String[] nativeWords = data.getStringArray(NATIVE_WORDS_KEY);
        String[] foreignWords = data.getStringArray(FOREIGN_WORDS_KEY);

        assert cellValues != null;
        int arraySize = cellValues.length;
        int wordListSize = nativeWords.length;

        //Writing game constants
        editor.putString(DIFFICULTY_KEY, difficulty.name());
        editor.putString(MODE_KEY, mode.name());
        editor.putInt(SUDOKU_SIZE_KEY, arraySize);
        editor.putInt(WORD_LIST_SIZE, wordListSize);

        //Writing cell data
        for (int i=0; i < arraySize; i++) {
            String index = Integer.toString(i);
            editor.putInt(VAlUES_KEY_PREFIX + index, cellValues[i]);
            editor.putInt(SOLUTION_KEY_PREFIX + index, solutionValues[i]);
            editor.putBoolean(LOCKED_KEY_PREFIX + index, lockedCells[i]);
        }

        //Writing word lists
        for (int i=0; i < wordListSize; i++) {
            String index = Integer.toString(i);
            editor.putString(NATIVE_KEY_PREFIX + index, nativeWords[i]);
            editor.putString(FOREIGN_KEY_PREFIX + index, foreignWords[i]);
        }

        //Make changes to xml file
        editor.apply();
    }

    /**
     * Load game data from file
     * @param context Caller
     * @return Game data bundle
     * @throws NullPointerException thrown if no valid file
     */
    public static Bundle loadGameData(Context context) throws NullPointerException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SAVE_GAME_FILE, Context.MODE_PRIVATE);
        Bundle data = new Bundle();

        Map<String, ?> dataMap = sharedPreferences.getAll();

        GameDifficulty difficulty = GameDifficulty.valueOf((String) dataMap.get(DIFFICULTY_KEY));
        GameMode mode = GameMode.valueOf((String) dataMap.get(MODE_KEY));

        //Loading game constants
        int size = (Integer) dataMap.get(SUDOKU_SIZE_KEY);
        int wordListSize = (Integer) dataMap.get(WORD_LIST_SIZE);

        int[] cellValues = new int[size];
        int[] solutionCells = new int[size];
        boolean[] lockedCells = new boolean[size];
        String[] nativeWords = new String[wordListSize];
        String[] foreignWords = new String[wordListSize];

        //Loading cell values
        for (int i = 0; i < size; i++) {
            String index = Integer.toString(i);
            cellValues[i] = (Integer) dataMap.get(VAlUES_KEY_PREFIX + index);
            solutionCells[i] = (Integer) dataMap.get(SOLUTION_KEY_PREFIX + index);
            lockedCells[i] = (Boolean) dataMap.get(LOCKED_KEY_PREFIX + index);
        }

        //Loading word list
        for (int i=0; i < wordListSize; i++) {
            String index = Integer.toString(i);
            nativeWords[i] = (String) dataMap.get(NATIVE_KEY_PREFIX + index);
            foreignWords[i] = (String) dataMap.get(FOREIGN_KEY_PREFIX + index);
        }

        data.putSerializable(DIFFICULTY_KEY, difficulty);
        data.putSerializable(MODE_KEY, mode);
        data.putIntArray(CELL_VALUES_KEY, cellValues);
        data.putIntArray(SOLUTION_VALUES_KEY, solutionCells);
        data.putBooleanArray(LOCKED_CELLS_KEY, lockedCells);
        data.putStringArray(NATIVE_WORDS_KEY, nativeWords);
        data.putStringArray(FOREIGN_WORDS_KEY, foreignWords);

        return data;
    }

    /**
     * Saves settings data
     * Saves default wordList if no valid word list provided
     * @param context caller
     * @param data bundle
     */
    public static void saveSettingsData(Context context, Bundle data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SAVE_SETTINGS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Unpacking "settings" bundle
        GameDifficulty difficulty = (GameDifficulty) data.getSerializable(DIFFICULTY_KEY);
        GameMode mode = (GameMode) data.getSerializable(MODE_KEY);
        boolean isAudioMode = data.getBoolean(AUDIO_KEY);
        boolean isSoundMode = data.getBoolean(SOUND_KEY);
        String[] nativeWords = data.getStringArray(NATIVE_WORDS_KEY);
        String[] foreignWords = data.getStringArray(FOREIGN_WORDS_KEY);

        //Try wordList
        int wordListSize;
        try {
            wordListSize = nativeWords.length;
        } catch (NullPointerException e) {
            Log.d(TAG, "saveSettingsData: Error saving word list settings data. Will load default word list");

            bundleWordListFromCSV(context, data);
            nativeWords = data.getStringArray(NATIVE_WORDS_KEY);
            foreignWords = data.getStringArray(FOREIGN_WORDS_KEY);
            wordListSize = nativeWords.length;
        }

        //Writing settings constants
        editor.putString(DIFFICULTY_KEY, difficulty.name());
        editor.putString(MODE_KEY, mode.name());
        editor.putString(AUDIO_KEY, Boolean.toString(isAudioMode));
        editor.putString(SOUND_KEY, Boolean.toString(isSoundMode));
        editor.putInt(WORD_LIST_SIZE, wordListSize);

        //Writing word lists
        for (int i = 0; i < wordListSize; i++) {
            String index = Integer.toString(i);
            editor.putString(NATIVE_KEY_PREFIX + index, nativeWords[i]);
            editor.putString(FOREIGN_KEY_PREFIX + index, foreignWords[i]);
        }

        //Make changes to xml file
        editor.apply();
    }

    /**
     * Loads settings data
     * Loads default settings if no valid settings
     * @param context caller
     * @return settings bundle
     */
    public static Bundle loadSettingsData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SAVE_SETTINGS_FILE, Context.MODE_PRIVATE);
        Map<String, ?> dataMap = sharedPreferences.getAll();
        Bundle data = new Bundle();

        try {
            //Loading game constants
            GameDifficulty difficulty = GameDifficulty.valueOf((String) dataMap.get(DIFFICULTY_KEY));
            GameMode mode = GameMode.valueOf((String) dataMap.get(MODE_KEY));
            boolean isAudioMode = Boolean.parseBoolean((String) dataMap.get(AUDIO_KEY));
            boolean isSoundMode = Boolean.parseBoolean((String) dataMap.get(SOUND_KEY));

            int wordListSize = (Integer) dataMap.get(WORD_LIST_SIZE);
            String[] nativeWords = new String[wordListSize];
            String[] foreignWords = new String[wordListSize];

            //Loading word list
            for (int i = 0; i < wordListSize; i++) {
                String index = Integer.toString(i);
                nativeWords[i] = (String) dataMap.get(NATIVE_KEY_PREFIX + index);
                foreignWords[i] = (String) dataMap.get(FOREIGN_KEY_PREFIX + index);
            }

            //Bundling settings
            data.putSerializable(DIFFICULTY_KEY, difficulty);
            data.putSerializable(MODE_KEY, mode);
            data.putBoolean(AUDIO_KEY, isAudioMode);
            data.putBoolean(SOUND_KEY, isSoundMode);
            data.putStringArray(NATIVE_WORDS_KEY, nativeWords);
            data.putStringArray(FOREIGN_WORDS_KEY, foreignWords);
        } catch (NullPointerException e) {
            Log.d(TAG, "loadSettingsData: Error loading settings data. Will load default settings");
            data = getDefaultSettings(context);
        }

        return data;
    }

    private static Bundle getDefaultSettings(Context context) {
        Bundle data = new Bundle();

        //Making settings bundle
        data.putSerializable(DIFFICULTY_KEY, GameDifficulty.MEDIUM);
        data.putSerializable(MODE_KEY, GameMode.NATIVE);
        data.putBoolean(AUDIO_KEY, false);
        data.putBoolean(SOUND_KEY, true);
        bundleWordListFromCSV(context, data);

        return data;
    }

    private static void bundleWordListFromCSV(Context context, Bundle settings) {
        ArrayList<String> nativeWordList = new ArrayList<>();
        ArrayList<String> foreignWordList = new ArrayList<>();

        try {
            //Opening file
            InputStream inputStream = context.getResources().openRawResource(R.raw.worlist_default);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {

                //Parsing file
                int separatorIndex = line.indexOf(',');
                if (separatorIndex != -1) {
                    nativeWordList.add(line.substring(0, separatorIndex));
                    foreignWordList.add(line.substring(separatorIndex + 1));
                }
            }

            //Cleaning Up
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();

        } catch (IOException e) {
            Log.e("MenuActivity", "readWordListFromCSV: cannot read file");
        }

        String[] nWords = new String[nativeWordList.size()];
        String[] fWords = new String[foreignWordList.size()];

        settings.putStringArray(NATIVE_WORDS_KEY, nativeWordList.toArray(nWords));
        settings.putStringArray(FOREIGN_WORDS_KEY, foreignWordList.toArray(fWords));
    }
}
