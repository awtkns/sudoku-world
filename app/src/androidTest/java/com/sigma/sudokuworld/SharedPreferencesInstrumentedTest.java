package com.sigma.sudokuworld;

import android.content.Context;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.sigma.sudokuworld.game.GameDifficulty;
import com.sigma.sudokuworld.game.GameMode;
import com.sigma.sudokuworld.persistence.sharedpreferences.PersistenceService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants.AUDIO_KEY;
import static com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants.DIFFICULTY_KEY;
import static com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants.HINTS_KEY;
import static com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants.MODE_KEY;
import static com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants.SOUND_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class SharedPreferencesInstrumentedTest {
    private Context context;

    @Before
    public void getContext() {
        context = InstrumentationRegistry.getContext();
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void readWriteSettings() {
        Bundle data = new Bundle();

        GameMode gameMode = GameMode.FOREIGN;
        GameDifficulty difficulty = GameDifficulty.HARD;
        boolean isAudioMode = false;
        boolean isSoundOn = true;
        boolean isHintsOn = false;


        //Making settings bundle
        data.putSerializable(DIFFICULTY_KEY, difficulty);
        data.putSerializable(MODE_KEY, gameMode);
        data.putBoolean(AUDIO_KEY, isAudioMode);
        data.putBoolean(SOUND_KEY, isSoundOn);
        data.putBoolean(HINTS_KEY, isHintsOn);

        //Making sure that serializable data works
        assertEquals(difficulty, data.getSerializable(DIFFICULTY_KEY));
        assertEquals(gameMode, data.getSerializable(MODE_KEY));

        PersistenceService.saveSettingsData(context, data);
        Bundle settings = PersistenceService.loadSettingsData(context);

        assertEquals(difficulty, settings.getSerializable(DIFFICULTY_KEY));
        assertEquals(gameMode, settings.getSerializable(MODE_KEY));
        assertEquals(isAudioMode, settings.getSerializable(AUDIO_KEY));
        assertEquals(isSoundOn, settings.getSerializable(SOUND_KEY));
        assertEquals(isHintsOn, settings.getSerializable(HINTS_KEY));
        assertNotEquals(!isHintsOn, settings.getSerializable(HINTS_KEY));
    }
}