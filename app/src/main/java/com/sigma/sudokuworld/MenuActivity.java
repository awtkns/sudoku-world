package com.sigma.sudokuworld;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.sigma.sudokuworld.persistence.KeyConstants;
import com.sigma.sudokuworld.persistence.PersistenceService;
import com.sigma.sudokuworld.audio.SoundPlayer;
import com.sigma.sudokuworld.sudoku.AudioSudokuActivity;
import com.sigma.sudokuworld.sudoku.VocabSudokuActivity;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = "MENU";
    private static final int REQUEST_CODE = 1;

    private SoundPlayer mSoundPlayer;
    private SettingsFragment mSettingsFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //HIDE STATUS BAR
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mSoundPlayer = new SoundPlayer(this);
        mSettingsFragment = (SettingsFragment) getFragmentManager().findFragmentById(R.id.settingsFragment);
        ImageView imageView = findViewById(R.id.menuAVD);
        AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) imageView.getDrawable();
        animatedVectorDrawable.start();
    }

    /**
     * Called when play button is pressed. (Action defined in xml onClick)
     */
    public void onPlayPressed(View v) {
        //Settings bundle
        Bundle settings = PersistenceService.loadSettingsData(getBaseContext());
        boolean isAudioMode = settings.getBoolean(KeyConstants.AUDIO_KEY, true);

        Intent intent;
        if (isAudioMode) {
            intent = new Intent(getBaseContext(), AudioSudokuActivity.class);
        } else {
            intent = new Intent(getBaseContext(), VocabSudokuActivity.class);
        }

        intent.putExtras(settings);
        intent.putExtra(KeyConstants.CONTINUE_KEY, false);

        mSoundPlayer.playPlaceCellSound();
        startActivity(intent);
    }

    /**
     * Called when Continue button is pressed. (Action defined in xml onClick)
     */
    public void onContinuePressed(View v) {
        try {
            Bundle settings = PersistenceService.loadSettingsData(getBaseContext());

            Intent intent;
            if (settings.getBoolean(KeyConstants.AUDIO_KEY,false )) {
               //Start in audio mode
               intent = new Intent(getBaseContext(), AudioSudokuActivity.class);
            } else {
               intent = new Intent(getBaseContext(), VocabSudokuActivity.class);
            }

            intent.putExtras(PersistenceService.loadGameData(getBaseContext()));
            intent.putExtra(KeyConstants.CONTINUE_KEY, true);
            mSoundPlayer.playPlaceCellSound();
            Log.d(TAG, "onContinueClick: starting game with data");
            startActivity(intent);
        } catch (NullPointerException e) {
            mSoundPlayer.playPlaceCellSound();
            Log.d(TAG, "onContinueClick: no game data");
        }
    }

    /**
     * Called when Settings button is pressed. (Action defined in xml onClick)
     */
    public void onSettingsPressed(View v) {
        mSoundPlayer.playPlaceCellSound();
        mSettingsFragment.showSettings();
    }

    @Override
    public void onBackPressed() {
        //If the settingsFragment cannot be hidden, then settings is not open therefore we minimize the app
        if (!mSettingsFragment.hideSettings()){
            this.moveTaskToBack(true);
        }
    }
}

