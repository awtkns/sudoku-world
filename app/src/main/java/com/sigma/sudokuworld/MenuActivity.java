package com.sigma.sudokuworld;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.sigma.sudokuworld.game.GameDifficulty;
import com.sigma.sudokuworld.game.GameMode;
import com.sigma.sudokuworld.game.gen.PuzzleGenerator;
import com.sigma.sudokuworld.persistence.GameRepository;
import com.sigma.sudokuworld.persistence.db.entities.Game;
import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;
import com.sigma.sudokuworld.persistence.sharedpreferences.PersistenceService;
import com.sigma.sudokuworld.audio.SoundPlayer;
import com.sigma.sudokuworld.sudoku.AudioSudokuActivity;
import com.sigma.sudokuworld.sudoku.VocabSudokuActivity;

import static com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants.DIFFICULTY_KEY;
import static com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants.MODE_KEY;
import static com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants.SAVE_KEY;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = "MENU";
    private SoundPlayer mSoundPlayer;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //HIDE STATUS BAR
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mSoundPlayer = new SoundPlayer(this);
        ImageView imageView = findViewById(R.id.menuAVD);
        AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) imageView.getDrawable();
        animatedVectorDrawable.start();
    }

    /**
     * Called when play button is pressed. (Action defined in xml onClick)
     */
    public void onPlayPressed(View v) {
        Bundle settings = PersistenceService.loadSettingsData(getBaseContext());    //TODO: refactor

        //New game
        GameDifficulty  difficulty = (GameDifficulty) settings.getSerializable(DIFFICULTY_KEY);
        GameMode gameMode = (GameMode) settings.getSerializable(MODE_KEY);
        PuzzleGenerator robot = new PuzzleGenerator(3);
        Bundle puzzle = robot.generatePuzzle(difficulty);

        Game game = new Game(
                //SaveID 0 = auto generate
                0, 0,
                difficulty,
                gameMode,
                puzzle.getIntArray(KeyConstants.CELL_VALUES_KEY),
                puzzle.getIntArray(KeyConstants.SOLUTION_VALUES_KEY),
                puzzle.getBooleanArray(KeyConstants.LOCKED_CELLS_KEY)
        );

        GameRepository repository = new GameRepository(getApplication());
        int saveID = repository.newGame(game);

        startGame(saveID);
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

    public void startGame(int saveID) {
        //Settings bundle
        Bundle settings = PersistenceService.loadSettingsData(getBaseContext());
        boolean isAudioMode = settings.getBoolean(KeyConstants.AUDIO_KEY, true);

        Intent intent;
        if (isAudioMode) {
            intent = new Intent(getBaseContext(), AudioSudokuActivity.class);
        } else {
            intent = new Intent(getBaseContext(), VocabSudokuActivity.class);
        }

        intent.putExtra(SAVE_KEY, saveID);
        mSoundPlayer.playPlaceCellSound();
        startActivity(intent);
    }
}

