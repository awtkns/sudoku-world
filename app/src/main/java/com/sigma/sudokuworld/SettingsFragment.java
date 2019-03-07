package com.sigma.sudokuworld;
import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;
import com.sigma.sudokuworld.persistence.sharedpreferences.PersistenceService;
import com.sigma.sudokuworld.game.GameDifficulty;
import com.sigma.sudokuworld.game.GameMode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsFragment extends Fragment {
    private RadioGroup mGameModeRadioGroup;
    private Switch mAudioModeSwitch;
    private Switch mSoundSwitch;
    private Switch mHintsSwitch;
    private SeekBar mDifficultySeekBar;
    private TextView mTextView;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStace) {
        //Hidden when the app is started and is only shown when the settings button is clicked
        mView = inflater.inflate(R.layout.fragment_settings, container, false);
        loadSettings();
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveSettings();
    }

    private void loadSettings(){
        mGameModeRadioGroup = mView.findViewById(R.id.gameModeRadioGroup);
        mAudioModeSwitch = mView.findViewById(R.id.audioModeSwitch);
        mSoundSwitch = mView.findViewById(R.id.soundSwitch);
        mHintsSwitch = mView.findViewById(R.id.hintsSwitch);
        mDifficultySeekBar = mView.findViewById(R.id.difficultyBar);

        Bundle previousSettings = PersistenceService.loadSettingsData(getActivity());
        GameDifficulty gameDifficulty = (GameDifficulty) previousSettings.getSerializable(KeyConstants.DIFFICULTY_KEY);
        GameMode gameMode = (GameMode) previousSettings.getSerializable(KeyConstants.MODE_KEY);
        boolean isAudioMode = previousSettings.getBoolean(KeyConstants.AUDIO_KEY);
        boolean isSoundMode = previousSettings.getBoolean(KeyConstants.SOUND_KEY);
        boolean isHintsMode = previousSettings.getBoolean(KeyConstants.HINTS_KEY);

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

        if (isSoundMode) {
            mSoundSwitch.setChecked(true);
        } else {
            mSoundSwitch.setChecked(false);
        }

        if (isHintsMode) {
            mHintsSwitch.setChecked(true);
        } else {
            mHintsSwitch.setChecked(false);
        }

        if (gameMode == GameMode.NATIVE) {
            mGameModeRadioGroup.check(R.id.nativeModeRadioButton);
        } else if (gameMode == GameMode.FOREIGN) {
            mGameModeRadioGroup.check(R.id.foreignModeRadioButton);
        } else {
            mGameModeRadioGroup.check(R.id.numbersModeRadioButton);
        }
    }

    private void saveSettings(){
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
        settingsBundle.putBoolean(KeyConstants.SOUND_KEY, mSoundSwitch.isChecked());
        settingsBundle.putBoolean(KeyConstants.HINTS_KEY, mHintsSwitch.isChecked());
        PersistenceService.saveSettingsData(getActivity(), settingsBundle);
    }
}
