package com.sigma.sudokuworld;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.sigma.sudokuworld.persistence.db.entities.Language;
import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;
import com.sigma.sudokuworld.persistence.sharedpreferences.PersistenceService;
import com.sigma.sudokuworld.game.GameDifficulty;
import com.sigma.sudokuworld.game.GameMode;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup mGameModeRadioGroup;
    private Switch mAudioModeSwitch;
    private SeekBar mDifficultySeekBar;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mGameModeRadioGroup = findViewById(R.id.gameModeRadioGroup);
        mAudioModeSwitch = findViewById(R.id.audioModeSwitch);
        mDifficultySeekBar = findViewById(R.id.difficultyBar);
        textView = findViewById(R.id.textView3);

        Bundle previousSettings = PersistenceService.loadSettingsData(SettingsActivity.this);
        GameDifficulty gameDifficulty = (GameDifficulty) previousSettings.getSerializable(KeyConstants.DIFFICULTY_KEY);
        GameMode gameMode = (GameMode) previousSettings.getSerializable(KeyConstants.MODE_KEY);
        boolean isAudioMode = previousSettings.getBoolean(KeyConstants.AUDIO_KEY);

        if (gameDifficulty == GameDifficulty.EASY) {
            mDifficultySeekBar.setProgress(0);
        } else if (gameDifficulty == GameDifficulty.MEDIUM) {
            mDifficultySeekBar.setProgress(1);
        } else  if (gameDifficulty == GameDifficulty.HARD) {
            mDifficultySeekBar.setProgress(2);
        }

        if (isAudioMode) {
            mAudioModeSwitch.setChecked(true);
        } else {
            mAudioModeSwitch.setChecked(false);
        }

        if (gameMode == GameMode.NATIVE) {
            mGameModeRadioGroup.check(R.id.nativeModeRadioButton);
        } else if (gameMode == GameMode.FOREIGN) {
            mGameModeRadioGroup.check(R.id.foreignModeRadioButton);
        } else {
            mGameModeRadioGroup.check(R.id.numbersModeRadioButton);
        }

        SudokuApplication app = (SudokuApplication) getApplication();
        List<Language> languages = app.getDB().getLanguageDao().getAll();

        String str = "[WIP] Language database test:";
        for (Language language: languages) {
            str += "\nEntry: " + language.getLanguageID() + " lang: " + language.getName() + " code: " + language.getCode();
        }
        textView.setText(str);
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
        settingsBundle.putBoolean(KeyConstants.AUDIO_KEY, mAudioModeSwitch.isChecked());
        PersistenceService.saveSettingsData(SettingsActivity.this, settingsBundle);

        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
