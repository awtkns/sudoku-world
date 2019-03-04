package com.sigma.sudokuworld;
import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.SudokuApplication;
import com.sigma.sudokuworld.db.Language;
import com.sigma.sudokuworld.persistence.KeyConstants;
import com.sigma.sudokuworld.persistence.PersistenceService;
import com.sigma.sudokuworld.game.GameDifficulty;
import com.sigma.sudokuworld.game.GameMode;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;


import java.util.List;

public class SettingsFragment extends Fragment {
    private RadioGroup mGameModeRadioGroup;
    private Switch mAudioModeSwitch;
    private SeekBar mDifficultySeekBar;
    private TextView mTextView;
    private View mView;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStace) {
        //Hidden when the app is started and is only shown when the settings button is clicked
        mView = inflater.inflate(R.layout.fragment_settings, container, false);
        mView.setVisibility(View.INVISIBLE);
        loadSettings();
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveSettings();
    }

    public void showSettings(){
        loadSettings();
        mView.setVisibility(View.VISIBLE);
    }

    public boolean hideSettings(){
        //If settings is open, save what we have
        if (mView.getVisibility() == View.VISIBLE) {
            mView.setVisibility(View.INVISIBLE);
            saveSettings();
            return true;

        //Settings is already hidden so do nothing
        } else {
            return false;
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
        PersistenceService.saveSettingsData(getActivity(), settingsBundle);
    }

    private void loadSettings(){
        mGameModeRadioGroup = mView.findViewById(R.id.gameModeRadioGroup);
        mAudioModeSwitch = mView.findViewById(R.id.audioModeSwitch);
        mDifficultySeekBar = mView.findViewById(R.id.difficultyBar);
        mTextView = mView.findViewById(R.id.textView3);

        Bundle previousSettings = PersistenceService.loadSettingsData(getActivity());
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

        SudokuApplication app = (SudokuApplication) getActivity().getApplication();
        List<Language> languages = app.getDB().getLanguageDao().getAll();

        String str = "[WIP] Language database test:";
        for (Language language: languages) {
            str += "\nEntry: " + language.getLanguageID() + " lang: " + language.getName() + " code: " + language.getCode();
        }
        mTextView.setText(str);
    }
}
