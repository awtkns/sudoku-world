package com.sigma.sudokuworld.sudoku;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;

import java.util.Locale;

public class AudioSudokuActivity extends SudokuActivity {

    private TextToSpeech mTTS;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTTS();

        super.mSudokuGridView.setOnLongClickListener(longClickListener);
    }

    @Override
    protected void onDestroy() {
        if(mTTS != null){
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }

    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (mVocabGame.isLockedCell(cellTouched)) {
                String text = mVocabGame.getCellString(cellTouched, false);
                mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }

            return true;
        }
    };

    private void initTTS() {
        //Initializing mTTS
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    Locale locale = new Locale("en");
                    mTTS.setLanguage(locale);
                }
            }
        });
    }
}
