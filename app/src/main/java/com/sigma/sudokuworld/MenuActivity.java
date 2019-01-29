package com.sigma.sudokuworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    Button mPlayButton;

    String[] englishWords = {"Black", "Red", "Yellow", "Green"};
    String[] frenchWords = {"Noir", "Rouge", "Jaune", "Vert"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mPlayButton = findViewById(R.id.playButton);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SudokuActivity.class);
                intent.putExtra("native", englishWords);
                intent.putExtra("foreign", frenchWords);
                startActivity(intent);
            }
        });

    }
}
