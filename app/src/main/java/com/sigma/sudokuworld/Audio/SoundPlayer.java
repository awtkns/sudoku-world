package com.sigma.sudokuworld.Audio;
import com.sigma.sudokuworld.R;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {

    private static SoundPool gameSounds;
    private static int emptyButtonSound;
    private static int placeCellSound;
    private static int wrongSound;
    private static int correctSound;
    private static int clearCellSound;

    public  SoundPlayer(Context context){

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        gameSounds = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(2)
                .build();

        emptyButtonSound = gameSounds.load(context, R.raw.emptybutton, 1);
        placeCellSound = gameSounds.load(context, R.raw.placecell, 1);
        wrongSound = gameSounds.load(context, R.raw.wrong, 1);
        correctSound = gameSounds.load(context, R.raw.correct, 1);
        clearCellSound = gameSounds.load(context, R.raw.clearcell, 1);
    }

    public void playEmptyButtonSound(){
        gameSounds.play(emptyButtonSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playPlaceCellSound() {
        gameSounds.play(placeCellSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playWrongSound() {
        gameSounds.play(wrongSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playCorrectSound(){
        gameSounds.play(correctSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playClearCellSound() {
        gameSounds.play(clearCellSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
