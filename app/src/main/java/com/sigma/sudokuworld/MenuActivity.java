package com.sigma.sudokuworld;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    Button mPlayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                intent.putExtra("native", readWordlistFromCSV(WordType.NATIVE));
                intent.putExtra("foreign", readWordlistFromCSV(WordType.FOREIGN));
                intent.putExtra("puzzle", readPuzzleDataFromCSV());
                startActivity(intent);
            }
        });
    }

    /**
     * Reads puzzle data from csv file "simplepuzzles.csv"
     * Stores data in an array.
     * Array index = cell number
     * Stores 0 for empty cell
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
            puzzleStr = puzzleStr.replace(",","");  //input sanitation

            vals = new int[puzzleStr.length()];
            for (int i = 0; i < puzzleStr.length(); i++){   //read puzzle data
                char ch =puzzleStr.charAt(i);
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
     * @param wordType get the native or foreign list
     * @return string array of words in either the native or foreign lang
     */
    private String[] readWordlistFromCSV(WordType wordType) {
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
