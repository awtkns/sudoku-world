package com.sigma.sudokuworld;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sigma.sudokuworld.audio.SoundPlayer;
import com.sigma.sudokuworld.persistence.db.entities.Game;
import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;
import com.sigma.sudokuworld.sudoku.AudioSudokuActivity;
import com.sigma.sudokuworld.sudoku.VocabSudokuActivity;
import com.sigma.sudokuworld.viewmodels.MenuViewModel;

import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private MenuViewModel mMenuViewModel;
    private SoundPlayer mSoundPlayer;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mMenuViewModel = ViewModelProviders.of(this).get(MenuViewModel.class);
        mFragmentManager = getSupportFragmentManager();

        //Play a sound every time the fragment is opened
        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                mSoundPlayer.playPlaceCellSound();
            }
        });

        mMenuViewModel.getAllGameSaves().observe(this, new Observer<List<Game>>() {
            @Override
            public void onChanged(@Nullable List<Game> games) {
                if (games.isEmpty()) findViewById(R.id.continueButton).setVisibility(View.GONE);
                else findViewById(R.id.continueButton).setVisibility(View.VISIBLE);
            }
        });


        mSoundPlayer = new SoundPlayer(this);
        ImageView imageView = findViewById(R.id.menuAVD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) imageView.getDrawable();
            animatedVectorDrawable.start();
        }
    }

    /**
     * Called when play button is pressed. (Action defined in xml onClick)
     */
    public void onPlayPressed(View v) {
        mFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, new NewGameFragment())
                .addToBackStack(null)
                .commit();
    }

    /**
     * Called when Continue button is pressed. (Action defined in xml onClick)
     */
    public void onContinuePressed(View v) {
        mFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, new SelectGameFragment())
                .addToBackStack(null)
                .commit();
    }

    /**
     * Called when Settings button is pressed. (Action defined in xml onClick)
     */
    public void onSettingsPressed(View v) {
        mFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, new SettingsFragment())
                .addToBackStack(null)
                .commit();
    }

    public void startGame(long saveID) {

        Intent intent;
        if (mMenuViewModel.isAudioModeEnabled) {
            intent = new Intent(getBaseContext(), AudioSudokuActivity.class);
        } else {
            intent = new Intent(getBaseContext(), VocabSudokuActivity.class);
        }

        intent.putExtra(KeyConstants.SAVE_ID_KEY, saveID);

        startActivity(intent);
        mFragmentManager.popBackStack();
    }

    public void closeFragment(){
        mFragmentManager.popBackStack();
    }
}

