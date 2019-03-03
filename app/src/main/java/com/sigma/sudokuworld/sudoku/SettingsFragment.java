package com.sigma.sudokuworld.sudoku;
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
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStace) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        super.onCreate(savedInstanceStace);

        mGameModeRadioGroup = view.findViewById(R.id.gameModeRadioGroup);
        mAudioModeSwitch = view.findViewById(R.id.audioModeSwitch);
        mDifficultySeekBar = view.findViewById(R.id.difficultyBar);
        textView = view.findViewById(R.id.textView3);

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
        textView.setText(str);
        return view;
    }

}
