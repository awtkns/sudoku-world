package com.sigma.sudokuworld;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.sigma.sudokuworld.game.GameDifficulty;
import com.sigma.sudokuworld.game.GameMode;
import com.sigma.sudokuworld.persistence.sharedpreferences.PersistenceService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@SuppressWarnings("ConstantConditions")
@RunWith(AndroidJUnit4.class)
public class SharedPreferencesInstrumentedTest {
    private Context context;

    @Before
    public void getContext() {
        context = InstrumentationRegistry.getContext();
    }


    @Test
    public void readWriteDifficulty() {
        GameDifficulty difficulty = GameDifficulty.HARD;
        PersistenceService.saveDifficultySetting(context, difficulty);
        assertEquals(difficulty, PersistenceService.loadDifficultySetting(context));

        difficulty = GameDifficulty.MEDIUM;
        PersistenceService.saveDifficultySetting(context, difficulty);
        assertEquals(difficulty, PersistenceService.loadDifficultySetting(context));

        difficulty = GameDifficulty.EASY;
        PersistenceService.saveDifficultySetting(context, difficulty);
        assertEquals(difficulty, PersistenceService.loadDifficultySetting(context));
    }

    @Test
    public void readWriteGameMode() {
        GameMode gameMode = GameMode.FOREIGN;
        PersistenceService.saveGameModeSetting(context, gameMode);
        assertEquals(gameMode, PersistenceService.loadGameModeSetting(context));
        assertNotEquals(GameMode.opposite(gameMode), PersistenceService.loadGameModeSetting(context));

        gameMode = GameMode.NATIVE;
        PersistenceService.saveGameModeSetting(context, gameMode);
        assertEquals(gameMode, PersistenceService.loadGameModeSetting(context));
        assertNotEquals(GameMode.opposite(gameMode), PersistenceService.loadGameModeSetting(context));

        gameMode = GameMode.NUMBERS;
        PersistenceService.saveGameModeSetting(context, gameMode);
        assertEquals(gameMode, PersistenceService.loadGameModeSetting(context));
    }

    @Test
    public void readWriteAudioMode() {
        boolean isAudioMode = false;
        PersistenceService.saveAudioModeEnableSetting(context, isAudioMode);
        assertEquals(isAudioMode, PersistenceService.loadAudioModeSetting(context));
        assertNotEquals(!isAudioMode, PersistenceService.loadAudioModeSetting(context));

        isAudioMode = true;
        PersistenceService.saveAudioModeEnableSetting(context, isAudioMode);
        assertEquals(isAudioMode, PersistenceService.loadAudioModeSetting(context));
        assertNotEquals(!isAudioMode, PersistenceService.loadAudioModeSetting(context));
    }

    @Test
    public void readWriteSoundEnabled() {
        boolean isSoundOn = true;

        PersistenceService.saveSoundEnabledSetting(context, isSoundOn);
        assertEquals(isSoundOn, PersistenceService.loadSoundEnabledSetting(context));
        assertNotEquals(!isSoundOn, PersistenceService.loadSoundEnabledSetting(context));

        isSoundOn = true;
        PersistenceService.saveSoundEnabledSetting(context, isSoundOn);
        assertEquals(isSoundOn, PersistenceService.loadSoundEnabledSetting(context));
        assertNotEquals(!isSoundOn, PersistenceService.loadSoundEnabledSetting(context));
    }

    @Test
    public void readWriteHintsEnabled() {
        boolean isHintsOn = false;

        PersistenceService.saveHintsEnabledSetting(context, isHintsOn);
        assertEquals(isHintsOn, PersistenceService.loadHintsEnabledSetting(context));
        assertNotEquals(!isHintsOn, PersistenceService.loadHintsEnabledSetting(context));

        isHintsOn = true;
        PersistenceService.saveHintsEnabledSetting(context, isHintsOn);
        assertEquals(isHintsOn, PersistenceService.loadHintsEnabledSetting(context));
        assertNotEquals(!isHintsOn, PersistenceService.loadHintsEnabledSetting(context));
    }
}