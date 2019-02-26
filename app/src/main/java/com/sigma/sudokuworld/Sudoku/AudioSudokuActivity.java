package com.sigma.sudokuworld.Sudoku;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;


import com.sigma.sudokuworld.LanguageAdapter;
import com.sigma.sudokuworld.LanguageItem;
import com.sigma.sudokuworld.R;

import java.util.ArrayList;
import java.util.Locale;

public class AudioSudokuActivity extends SudokuActivity {

    private TextToSpeech mTTS;
    private ArrayList<LanguageItem> mLanguageList;
    private String clickedLanguage;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initList();
        initTTS();

        LanguageAdapter languageAdapter = new LanguageAdapter(this, mLanguageList);
        Spinner spinnerLanguages = findViewById(R.id.spinner_languages);
        spinnerLanguages.setAdapter(languageAdapter);
        spinnerLanguages.setOnItemSelectedListener(itemSelectedListener);

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

    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            LanguageItem clickedItem = (LanguageItem)parent.getItemAtPosition(position);
            clickedLanguage = clickedItem.getLanguageName();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void initList(){
        mLanguageList = new ArrayList<>();
        mLanguageList.add(new LanguageItem("en", R.drawable.vd_logo));
        mLanguageList.add(new LanguageItem("fr", R.drawable.vd_logo));
    }

    private void initTTS() {
        //Initializing mTTS
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    Locale locale = new Locale(clickedLanguage);
                    mTTS.setLanguage(locale);
                }
            }
        });
    }
}
