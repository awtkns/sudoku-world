package com.sigma.sudokuworld;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.sigma.sudokuworld.Persistence.KeyConstants;
import com.sigma.sudokuworld.Persistence.PersistenceService;
import com.sigma.sudokuworld.VocabGame.GameDifficulty;
import com.sigma.sudokuworld.VocabGame.GameMode;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup mGameModeRadioGroup;
    private SeekBar mDifficultySeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mGameModeRadioGroup = findViewById(R.id.gameModeRadioGroup);
        mDifficultySeekBar = findViewById(R.id.difficultyBar);

        Bundle previousSettings = PersistenceService.loadSettingsData(SettingsActivity.this);
        GameDifficulty gameDifficulty = (GameDifficulty) previousSettings.getSerializable(KeyConstants.DIFFICULTY_KEY);
        GameMode gameMode = (GameMode) previousSettings.getSerializable(KeyConstants.MODE_KEY);

        if (gameDifficulty == GameDifficulty.EASY) {
            mDifficultySeekBar.setProgress(0);
        } else if (gameDifficulty == GameDifficulty.MEDIUM) {
            mDifficultySeekBar.setProgress(1);
        } else  if (gameDifficulty == GameDifficulty.HARD) {
            mDifficultySeekBar.setProgress(2);
        }

        if (gameMode == GameMode.NATIVE) {
            mGameModeRadioGroup.check(R.id.nativeModeRadioButton);
        } else if (gameMode == GameMode.FOREIGN) {
            mGameModeRadioGroup.check(R.id.foreignModeRadioButton);
        } else {
            mGameModeRadioGroup.check(R.id.numbersModeRadioButton);
        }
    }

    @Override
    public void onBackPressed() {

        //Checking GameMode
        GameMode gameMode;
        int checkedRadioButtonID = mGameModeRadioGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonID == R.id.nativeModeRadioButton) {
            gameMode = GameMode.NATIVE;
        } else if (checkedRadioButtonID == R.id.foreignModeRadioButton) {
            gameMode = GameMode.FOREIGN;
        } else {
            gameMode = GameMode.NUMBERS;
        }

        //Checking Difficulty
        GameDifficulty gameDifficulty;
        int difficulty = mDifficultySeekBar.getProgress();
        if (difficulty == 1) {
            gameDifficulty = GameDifficulty.MEDIUM;
        } else if (difficulty == 2) {
            gameDifficulty = GameDifficulty.HARD;
        } else {
            gameDifficulty = GameDifficulty.EASY;
        }

        //Saving Data
        Bundle settingsBundle = new Bundle();
        settingsBundle.putSerializable(KeyConstants.DIFFICULTY_KEY, gameDifficulty);
        settingsBundle.putSerializable(KeyConstants.MODE_KEY, gameMode);
        PersistenceService.saveSettingsData(SettingsActivity.this, settingsBundle);

        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
