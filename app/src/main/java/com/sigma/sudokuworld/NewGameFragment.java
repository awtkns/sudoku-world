package com.sigma.sudokuworld;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import android.widget.TextView;
import com.sigma.sudokuworld.game.GameDifficulty;
import com.sigma.sudokuworld.game.GameMode;
import com.sigma.sudokuworld.persistence.db.entities.Set;
import com.sigma.sudokuworld.masterdetail.MasterSelectActivity;
import com.sigma.sudokuworld.viewmodels.MenuViewModel;

public class NewGameFragment extends Fragment {
    private MenuViewModel mMenuViewModel;
    private View mView;
    private SeekBar mDifficultySeekBar;
    private RadioGroup mGameModeRadioGroup;
    private View mSetLayout;
    private TextView mSetTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the menu's viewModel
        mMenuViewModel = ViewModelProviders.of(this).get(MenuViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_new_game, container, false);
        mGameModeRadioGroup = mView.findViewById(R.id.gameModeRadioGroup);
        mDifficultySeekBar = mView.findViewById(R.id.difficultyBar);
        mSetLayout = mView.findViewById(R.id.setViewLayout);
        mSetTitle = mView.findViewById(R.id.setTitle);

        Button setButton = mView.findViewById(R.id.setBuilderButton);
        setButton.setOnClickListener(setButtonListener);

        Button playButton = mView.findViewById(R.id.playNewGameButton);
        playButton.setOnClickListener(playButtonListener);

        Button cancelButton = mView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(cancelButtonListener);

        mGameModeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mView.findViewById(R.id.numbersModeRadioButton).getId()) {
                    mSetLayout.setVisibility(View.GONE);
                } else mSetLayout.setVisibility(View.VISIBLE);
            }
        });

        initStoredSettings();
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveSettings();
    }

    View.OnClickListener playButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((MenuActivity) getActivity()).startGame(mMenuViewModel.generateNewGameWithStoredSettings());
        }
    };

    View.OnClickListener setButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Checking GameMode
            Intent intent = new Intent(getActivity().getBaseContext(), MasterSelectActivity.class);
            startActivity(intent);      //TODO start activity for result
        }
    };

    View.OnClickListener cancelButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((MenuActivity) getActivity()).closeFragment();
        }
    };

    private void initStoredSettings() {

        Set set = mMenuViewModel.getSet();
        mSetTitle.setText(set.getName());

        GameDifficulty gameDifficulty = mMenuViewModel.getGameDifficulty();
        if (gameDifficulty == GameDifficulty.EASY) {
            mDifficultySeekBar.setProgress(0);
        } else if (gameDifficulty == GameDifficulty.MEDIUM) {
            mDifficultySeekBar.setProgress(1);
        } else  if (gameDifficulty == GameDifficulty.HARD) {
            mDifficultySeekBar.setProgress(2);
        }

        GameMode gameMode = mMenuViewModel.getGameMode();
        if (gameMode == GameMode.NATIVE) {
            mGameModeRadioGroup.check(R.id.nativeModeRadioButton);
        } else if (gameMode == GameMode.FOREIGN) {
            mGameModeRadioGroup.check(R.id.foreignModeRadioButton);
        } else {
            mGameModeRadioGroup.check(R.id.numbersModeRadioButton);
            mSetLayout.setVisibility(View.GONE);
        }
    }

    private void saveSettings(){
        //Saving Data
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
        int pos = mDifficultySeekBar.getProgress();
        if (pos == 1) {
            gameDifficulty = GameDifficulty.MEDIUM;
        } else if (pos == 2) {
            gameDifficulty = GameDifficulty.HARD;
        } else {
            gameDifficulty = GameDifficulty.EASY;
        }

        mMenuViewModel.setGameDifficulty(gameDifficulty);
        mMenuViewModel.setGameMode(gameMode);
    }
}
