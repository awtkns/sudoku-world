package com.sigma.sudokuworld;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import android.widget.TextView;
import com.sigma.sudokuworld.game.GameDifficulty;
import com.sigma.sudokuworld.game.GameMode;
import com.sigma.sudokuworld.masterdetail.MasterSelectActivity;
import com.sigma.sudokuworld.persistence.db.entities.Set;
import com.sigma.sudokuworld.viewmodels.MenuViewModel;

public class NewGameFragment extends Fragment {
    private MenuViewModel mMenuViewModel;
    private View mView;
    private SeekBar mDifficultySeekBar;
    private RadioGroup mGameModeRadioGroup;
    private View mSetLayout;
    private TextView mSetTitle;
    private Button mPlayButton;
    private Button mSetButton;
    private Button mCancelButton;

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

        mSetButton = mView.findViewById(R.id.setBuilderButton);
        mSetButton.setOnClickListener(setButtonListener);

        mPlayButton = mView.findViewById(R.id.playNewGameButton);
        mPlayButton.setOnClickListener(playButtonListener);

        mCancelButton = mView.findViewById(R.id.cancelButton);
        mCancelButton.setOnClickListener(cancelButtonListener);

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
    public void onStart() {
        super.onStart();

        Set set = mMenuViewModel.getSelectedSet(); //TODO make buttons disappear better
        if (set == null) {
            mSetTitle.setVisibility(View.GONE);
            mPlayButton.setVisibility(View.GONE);

            Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
            mSetButton.startAnimation(shake);
        } else {
            mSetTitle.setVisibility(View.VISIBLE);
            mPlayButton.setVisibility(View.VISIBLE);
            mSetTitle.setText(set.getName());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveSettings();
    }

    View.OnClickListener playButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveSettings();
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

        GameDifficulty difficulty = mMenuViewModel.getSelectedGameDifficulty();
        if (difficulty == GameDifficulty.EASY) {
            mDifficultySeekBar.setProgress(0);
        } else if (difficulty == GameDifficulty.MEDIUM) {
            mDifficultySeekBar.setProgress(1);
        } else {
            mDifficultySeekBar.setProgress(2);
        }

        GameMode mode = mMenuViewModel.getSelectedGameMode();
        if (mode == GameMode.NATIVE) {
            mGameModeRadioGroup.check(R.id.nativeModeRadioButton);
        } else if (mode == GameMode.FOREIGN) {
            mGameModeRadioGroup.check(R.id.foreignModeRadioButton);
        } else {
            mGameModeRadioGroup.check(R.id.numbersModeRadioButton);

            //Numbers mode selected hide set layout
            mSetLayout.setVisibility(View.GONE);
        }
    }

    private void saveSettings(){

        //Checking GameMode
        int checkedRadioButtonID = mGameModeRadioGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonID == R.id.nativeModeRadioButton) {
            mMenuViewModel.setSelectedGameMode(GameMode.NATIVE);
        } else if (checkedRadioButtonID == R.id.foreignModeRadioButton) {
            mMenuViewModel.setSelectedGameMode(GameMode.FOREIGN);
        } else {
            mMenuViewModel.setSelectedGameMode(GameMode.NUMBERS);
        }

        //Checking Difficulty
        int pos = mDifficultySeekBar.getProgress();
        if (pos == 0) {
            mMenuViewModel.setSelectedGameDifficulty(GameDifficulty.EASY);
        } else if (pos == 1) {
            mMenuViewModel.setSelectedGameDifficulty(GameDifficulty.MEDIUM);
        } else {
            mMenuViewModel.setSelectedGameDifficulty(GameDifficulty.HARD);
        }
    }
}
