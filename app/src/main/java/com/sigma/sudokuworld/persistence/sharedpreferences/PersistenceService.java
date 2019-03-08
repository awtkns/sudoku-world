package com.sigma.sudokuworld.persistence.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.sigma.sudokuworld.game.GameDifficulty;
import com.sigma.sudokuworld.game.GameMode;

import static com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants.*;


public abstract class PersistenceService {
    private static final String SAVE_SETTINGS_FILE = "settings";

    /* --- Saving --- */

    public static void saveDifficultySetting(Context context, GameDifficulty difficulty) {
        getEditor(context).putString(DIFFICULTY_KEY, difficulty.name()).apply();
    }

    public static void saveGameModeSetting(Context context, GameMode gameMode) {
        getEditor(context).putString(MODE_KEY, gameMode.name()).apply();
    }

    public static void saveAudioModeEnableSetting(Context context, boolean audioModeEnabled) {
        getEditor(context).putBoolean(AUDIO_KEY, audioModeEnabled).apply();
    }

    public static void saveSoundEnabledSetting(Context context, boolean soundEnabled) {
        getEditor(context).putBoolean(SOUND_KEY, soundEnabled).apply();
    }

    public static void saveHintsEnabledSetting(Context context, boolean hintsEnabled) {
        getEditor(context).putBoolean(HINTS_KEY, hintsEnabled).apply();
    }

    public static void saveRectangleModeEnabledSetting(Context context, boolean rectangleModeEnabled) {
        getEditor(context).putBoolean(RECTANGLE_KEY, rectangleModeEnabled).apply();
    }

    /* --- Loading --- */

    public static GameDifficulty loadDifficultySetting(Context context) {
        return GameDifficulty.fromString(getSettings(context).getString(DIFFICULTY_KEY, GameDifficulty.EASY.name()));
    }

    public static GameMode loadGameModeSetting(Context context) {
        return GameMode.fromString(getSettings(context).getString(MODE_KEY, GameMode.NUMBERS.name()));
    }

    public static boolean loadAudioModeSetting(Context context) {
        return getSettings(context).getBoolean(AUDIO_KEY, false);
    }

    public static boolean loadSoundEnabledSetting(Context context) {
        return getSettings(context).getBoolean(SOUND_KEY, true);
    }

    public static boolean loadHintsEnabledSetting(Context context) {
        return getSettings(context).getBoolean(HINTS_KEY, true);
    }

    public static boolean loadRectangleModeEnabledSetting(Context context) {
        return getSettings(context).getBoolean(RECTANGLE_KEY, false);
    }

    /*Private Helpers**/
    private static SharedPreferences.Editor getEditor(Context context) {
        return context.getSharedPreferences(SAVE_SETTINGS_FILE, Context.MODE_PRIVATE).edit();
    }

    private static SharedPreferences getSettings(Context context) {
        return  context.getSharedPreferences(SAVE_SETTINGS_FILE, Context.MODE_PRIVATE);
    }
}
