package com.sigma.sudokuworld;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.sigma.sudokuworld.Persistence.KeyConstants;
import com.sigma.sudokuworld.VocabGame.GameDifficulty;
import com.sigma.sudokuworld.VocabGame.GameMode;

public class SettingsActivity extends AppCompatActivity {

    RadioGroup mGameModeRadioGroup;
    SeekBar mDifficultySeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mGameModeRadioGroup = findViewById(R.id.modeSelector);
        mDifficultySeekBar = findViewById(R.id.difficultyBar);
    }

    @Override
    public void onBackPressed() {

        //Checking GameMode
        GameMode gameMode;
        int checkedRadioButtonID = mGameModeRadioGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonID == R.id.nativeMode) {
            gameMode = GameMode.NATIVE;
        } else if (checkedRadioButtonID == R.id.foreignMode) {
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

        Intent settings = getIntent();
        settings.putExtra(KeyConstants.MODE_KEY, gameMode);
        settings.putExtra(KeyConstants.DIFFICULTY_KEY, gameDifficulty);
        setResult(Activity.RESULT_OK, settings);
        finish();
    }
}
