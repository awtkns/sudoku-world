package com.sigma.sudokuworld.audio;
import com.sigma.sudokuworld.R;
import com.sigma.sudokuworld.persistence.sharedpreferences.PersistenceService;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

public class SoundPlayer {

    private static SoundPool gameSounds;
    private static AudioAttributes audioAttributes;
    private static int emptyButtonSound;
    private static int placeCellSound;
    private static int wrongSound;
    private static int correctSound;
    private static int clearCellSound;

    private static final float LEFT_VOLUME = 1;
    private static final int RIGHT_VOLUME = 1;
    private static final int PRIORITY = 1;
    private static final int LOOP = 0;
    private static final float RATE = 1;

    private Context mContext;
    public SoundPlayer(Context context){
        mContext = context;
        //Initializing our pool of sounds
        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        gameSounds = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(2)
                .build();

        
        //Linking our sounds with the right files in /raw
        emptyButtonSound = gameSounds.load(context, R.raw.emptybutton, 1);
        placeCellSound = gameSounds.load(context, R.raw.placecell, 1);
        wrongSound = gameSounds.load(context, R.raw.wrong, 1);
        correctSound = gameSounds.load(context, R.raw.correct, 1);
        clearCellSound = gameSounds.load(context, R.raw.clearcell, 1);
    }

    public void playEmptyButtonSound(){
        if (isSoundOn()){
            gameSounds.play(emptyButtonSound, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, LOOP, RATE);
        }
    }

    public void playPlaceCellSound() {
        if (isSoundOn()){
            gameSounds.play(placeCellSound, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, LOOP, RATE);
        }
    }

    public void playWrongSound() {
        if (isSoundOn()){
            gameSounds.play(wrongSound, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, LOOP, RATE);
        }
    }

    public void playCorrectSound(){
        if (isSoundOn()){
            gameSounds.play(correctSound, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, LOOP, RATE);
        }
    }

    public void playClearCellSound() {
        if (isSoundOn()){
            gameSounds.play(clearCellSound, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, LOOP, RATE);
        }
    }

    private boolean isSoundOn(){
        return PersistenceService.loadSoundEnabledSetting(mContext);
    }
}
