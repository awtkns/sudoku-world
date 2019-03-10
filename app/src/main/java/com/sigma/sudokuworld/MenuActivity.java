package com.sigma.sudokuworld;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.sigma.sudokuworld.audio.SoundPlayer;
import com.sigma.sudokuworld.sudoku.AudioSudokuActivity;
import com.sigma.sudokuworld.sudoku.VocabSudokuActivity;
import com.sigma.sudokuworld.viewmodels.MenuViewModel;

import static com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants.HINTS_KEY;
import static com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants.SAVE_ID_KEY;
import static com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants.SOUND_KEY;

public class MenuActivity extends AppCompatActivity {
    private MenuViewModel mMenuViewModel;
    private SoundPlayer mSoundPlayer;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mSoundPlayer = new SoundPlayer(this);
        ImageView imageView = findViewById(R.id.menuAVD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) imageView.getDrawable();
            animatedVectorDrawable.start();
        }

        mMenuViewModel = ViewModelProviders.of(this).get(MenuViewModel.class);
    }

    /**
     * Called when play button is pressed. (Action defined in xml onClick)
     */
    public void onPlayPressed(View v) {
        mSoundPlayer.playPlaceCellSound();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, new NewGameFragment())
                .addToBackStack(null)
                .commit();
    }

    /**
     * Called when Continue button is pressed. (Action defined in xml onClick)
     */
    public void onContinuePressed(View v) {
        mSoundPlayer.playPlaceCellSound();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, new SelectGameFragment())
                .addToBackStack(null)
                .commit();
    }

    /**
     * Called when Settings button is pressed. (Action defined in xml onClick)
     */
    public void onSettingsPressed(View v) {
        mSoundPlayer.playPlaceCellSound();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, new SettingsFragment())
                .addToBackStack(null)
                .commit();
    }

    public void startGame(long saveID) {

        Intent intent;
        boolean isAudioMode = mMenuViewModel.isAudioModeEnabled();
        if (isAudioMode) {
            intent = new Intent(getBaseContext(), AudioSudokuActivity.class);
        } else {
            intent = new Intent(getBaseContext(), VocabSudokuActivity.class);
        }

        mSoundPlayer.playPlaceCellSound();
        startActivity(intent);

        getSupportFragmentManager().popBackStack();
    }

    public void closeFragment(){
        getSupportFragmentManager().popBackStack();
        mSoundPlayer.playPlaceCellSound();
    }
}

