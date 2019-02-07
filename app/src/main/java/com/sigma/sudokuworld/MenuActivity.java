package com.sigma.sudokuworld;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sigma.sudokuworld.Persistence.KeyConstants;
import com.sigma.sudokuworld.Persistence.PersistenceService;
import com.sigma.sudokuworld.VocabGame.GameDifficulty;
import com.sigma.sudokuworld.VocabGame.GameMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;



public class MenuActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 1;
    private GameDifficulty gameDifficulty = GameDifficulty.EASY;
    private GameMode gameMode = GameMode.NUMBERS;

    Button mPlayButton;
    Button mContinueButton;
    Button mSettingsButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                //Adding information for the sudoku activity
                intent.putExtra(KeyConstants.NATIVE_WORDS_KEY, readWordListFromCSV(WordType.NATIVE));
                intent.putExtra(KeyConstants.FOREIGN_WORDS_KEY, readWordListFromCSV(WordType.FOREIGN));
                intent.putExtra(KeyConstants.MODE_KEY, gameMode);
                intent.putExtra(KeyConstants.DIFFICULTY_KEY, gameDifficulty);
                intent.putExtra(KeyConstants.CONTINUE_KEY, false);
                startActivity(intent);
            }
        });

        mContinueButton = findViewById(R.id.continueButton);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SudokuActivity.class);

                try {
                    intent.putExtras(PersistenceService.loadGameData(MenuActivity.this));
                    intent.putExtra(KeyConstants.CONTINUE_KEY, true);
                    Log.d("Game Data", "onClick: starting game with data");
                    startActivity(intent);
                } catch (Exception e) {
                    Log.d("Game Data", "onClick: no game data");
                }
            }
        });

        mSettingsButton = findViewById(R.id.settingsButton);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                gameDifficulty = (GameDifficulty) data.getSerializableExtra(KeyConstants.DIFFICULTY_KEY);
                gameMode = (GameMode) data.getSerializableExtra(KeyConstants.MODE_KEY);
            }
        }
    }

    /**
     * Reads puzzle data from csv file "simplepuzzles.csv"
     * Stores data in an array.
     * Array index = cell number
     * Stores 0 for empty cell
     *
     * @return array
     */
    private int[] readPuzzleDataFromCSV() {
        InputStream inputStream = getResources().openRawResource(R.raw.simplepuzzles);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


        int[] vals = new int[0];
        try {
            String puzzleStr;
            puzzleStr = bufferedReader.readLine();
            puzzleStr = puzzleStr.replace(",", "");  //input sanitation

            vals = new int[puzzleStr.length()];
            for (int i = 0; i < puzzleStr.length(); i++) {   //read puzzle data
                char ch = puzzleStr.charAt(i);
                if (ch != '.') {
                    vals[i] = Character.getNumericValue(ch);
                }
            }

        } catch (IOException e) {
            Log.e("MenuActivity", "readPuzzleDataFromCSV: cannot read file");
        }

        return vals;
    }

    private enum WordType {
        NATIVE, FOREIGN
    }

    /**
     * Reads CSV file containing 9 word pairs
     *
     * @param wordType get the native or foreign list
     * @return string array of words in either the native or foreign lang
     */
    private String[] readWordListFromCSV(WordType wordType) {
        InputStream inputStream = getResources().openRawResource(R.raw.wordlist);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line;
        ArrayList<String> wordList = new ArrayList<>();
        try {
            while ((line = bufferedReader.readLine()) != null) {
                if (wordType == WordType.NATIVE) {
                    int i = line.indexOf(',');

                    if (i != -1) {
                        wordList.add(line.substring(0, i));
                    }
                } else if (wordType == WordType.FOREIGN) {
                    int i = line.indexOf(',');

                    if (i != -1) {
                        wordList.add(line.substring(i + 1));
                    }
                }
            }
        } catch (IOException e) {
            Log.e("MenuActivity", "readWordListFromCSV: cannot read file");
        }

        String[] words = new String[wordList.size()];
        return wordList.toArray(words);
    }
}

