package com.sigma.sudokuworld;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.sigma.sudokuworld.Persistence.KeyConstants;
import com.sigma.sudokuworld.Persistence.PersistenceService;
import com.sigma.sudokuworld.Audio.SoundPlayer;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = "MENU";
    private static final int REQUEST_CODE = 1;

    Button mPlayButton;
    Button mContinueButton;
    Button mSettingsButton;
    SoundPlayer mSoundPlayer;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //HIDE STATUS BAR
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_menu);

        ImageView imageView = findViewById(R.id.menuAVD);
        AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) imageView.getDrawable();
        animatedVectorDrawable.start();

        //On play button click go to sudoku activity
        mPlayButton = findViewById(R.id.playButton);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SudokuActivity.class);

                //Settings bundle
                Bundle settings = PersistenceService.loadSettingsData(MenuActivity.this);
                intent.putExtras(settings);
                intent.putExtra(KeyConstants.CONTINUE_KEY, false);
                mSoundPlayer.playPlaceCellSound();
                startActivity(intent);
            }
        });

        mContinueButton = findViewById(R.id.continueButton);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MenuActivity.this, SudokuActivity.class);
                    intent.putExtras(PersistenceService.loadGameData(MenuActivity.this));
                    intent.putExtra(KeyConstants.CONTINUE_KEY, true);
                    mSoundPlayer.playPlaceCellSound();
                    Log.d(TAG, "onContinueClick: starting game with data");
                    startActivity(intent);
                } catch (Exception e) {
                    mSoundPlayer.playPlaceCellSound();
                    Log.d(TAG, "onContinueClick: no game data");
                }
            }
        });

        mSettingsButton = findViewById(R.id.settingsButton);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
                mSoundPlayer.playPlaceCellSound();
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        mSoundPlayer = new SoundPlayer(this);
    }

    //So the splash doesn't replay every time / the activity simply gets minimized on back press
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}

