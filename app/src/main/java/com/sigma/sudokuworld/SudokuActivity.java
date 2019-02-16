package com.sigma.sudokuworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import static com.sigma.sudokuworld.Persistence.KeyConstants.*;
import android.widget.Toast;

import com.sigma.sudokuworld.Persistence.PersistenceService;
import com.sigma.sudokuworld.VocabGame.GameDifficulty;
import com.sigma.sudokuworld.VocabGame.GameMode;
import com.sigma.sudokuworld.VocabGame.VocabSudokuModel;
import com.sigma.sudokuworld.Audio.SoundPlayer;

public class SudokuActivity extends AppCompatActivity {

    private VocabSudokuModel mVocabGame;
    private SudokuGridView mSudokuGridView;
    private Button[] mInputButtons;
    private SoundPlayer mSoundPlayer;
    private int cellTouched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //HIDE STATUS BAR
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sudoku);

        if (savedInstanceState != null) {
            //Unpacking information from saved instance
            mVocabGame = new VocabSudokuModel(
                    savedInstanceState.getStringArray(NATIVE_WORDS_KEY),
                    savedInstanceState.getStringArray(FOREIGN_WORDS_KEY),
                    savedInstanceState.getIntArray(CELL_VALUES_KEY),
                    savedInstanceState.getIntArray(SOLUTION_VALUES_KEY),
                    savedInstanceState.getBooleanArray(LOCKED_CELLS_KEY),
                    (GameMode) savedInstanceState.getSerializable(MODE_KEY),
                    (GameDifficulty) savedInstanceState.getSerializable(DIFFICULTY_KEY)
            );
        } else {
            //Unpacking information from intent
            Intent i = getIntent();

            if (i.getBooleanExtra(CONTINUE_KEY, false)) {
                //New game

                mVocabGame = new VocabSudokuModel(
                        i.getStringArrayExtra(NATIVE_WORDS_KEY),
                        i.getStringArrayExtra(FOREIGN_WORDS_KEY),
                        i.getIntArrayExtra(CELL_VALUES_KEY),
                        i.getIntArrayExtra(SOLUTION_VALUES_KEY),
                        i.getBooleanArrayExtra(LOCKED_CELLS_KEY),
                        (GameMode) i.getSerializableExtra(MODE_KEY),
                        (GameDifficulty) i.getSerializableExtra(DIFFICULTY_KEY)
                );
            } else {
                //Save game

                mVocabGame = new VocabSudokuModel(
                        i.getStringArrayExtra(NATIVE_WORDS_KEY),
                        i.getStringArrayExtra(FOREIGN_WORDS_KEY),
                        (GameMode) i.getSerializableExtra(MODE_KEY),
                        (GameDifficulty) i.getSerializableExtra(DIFFICULTY_KEY)
                );
            }
        }

        //Initializing Sudoku grid
        mSudokuGridView = findViewById(R.id.sudokuGrid_view);
        mSudokuGridView.setOnTouchListener(onSudokuGridTouchListener);

        if (mVocabGame.getGameMode() != GameMode.NUMBERS) {
            mSudokuGridView.setOnLongClickListener(longClickListener);
        }


        //Initializing buttons
        mInputButtons = new Button[9];
        for(int i = 0; i < mInputButtons.length; i++)
        {
            //Sets the button array at index to have id button + the current index number
            //One is added because the number 0 is skipped
            mInputButtons[i] = findViewById(getResources().getIdentifier("button" + (i+1), "id",
                    this.getPackageName()));

            //Gets and sets the string the button should display
            String buttonText = mVocabGame.getButtonString(i + 1);
            mInputButtons[i].setText(buttonText);

            //Links the listener to the button
            mInputButtons[i].setOnClickListener(onButtonClickListener);
        }

        mSoundPlayer = new SoundPlayer(this);

        updateAllViewLabels();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the current state of the Sudoku board
        makeSaveBundle(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Bundle data = new Bundle();
        makeSaveBundle(data);

        //Saves data to file
        PersistenceService.saveGameData(this, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //When sudoku grid is touched
    private SudokuGridView.OnTouchListener onSudokuGridTouchListener = new SudokuGridView.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int eventAction = event.getAction();
            boolean touchHandled = false;

            //Through looking at every case, we can move the highlight to where our finger moves to
            switch (eventAction) {
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_DOWN:
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    //If touch in the bound of the grid
                    if (mSudokuGridView.getGridBounds().contains(x, y)) {

                        //Clear previous highlighted cell
                        mSudokuGridView.clearHighlightedCell();

                        //Cell that was touched
                        int cellNum = mSudokuGridView.getCellNumberFromCoordinates(x, y);
                        cellTouched = cellNum;

                        //If we have selected the incorrect cell, un highlight it
                        if (cellNum == mSudokuGridView.getIncorrectCell()) {
                            mSudokuGridView.clearIncorrectCell();
                        }

                        //Set new highlighted cell if its not a locked cell
                        if (!mVocabGame.isLockedCell(cellNum)) {
                            mSudokuGridView.setHighlightedCell(cellNum);

                            //No long press
                            touchHandled = true;
                        }

                        //Force redraw view
                        mSudokuGridView.invalidate();
                        mSudokuGridView.performClick();
                    }
            }

            return touchHandled;
        }
    };

    SudokuGridView.OnLongClickListener longClickListener = new SudokuGridView.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (mVocabGame.isLockedCell(cellTouched)) {
                String text = mVocabGame.getCellString(cellTouched, false);
                Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
            }

            return true;
        }
    };

    private View.OnClickListener onButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            int buttonValue = 0;

            //Loop through all our possible buttons to see which button is clicked
            //Set buttonValue to the corresponding button
            for (int buttonIndex = 0; buttonIndex < 9; buttonIndex++) {
                if (button == mInputButtons[buttonIndex]){
                    buttonValue = buttonIndex + 1;
                }
            }

            int cellNumber = mSudokuGridView.getHighlightedCell();

            //No cell is highlighted
            if (cellNumber == -1){
                mSoundPlayer.playEmptyButtonSound();
            } else {
                mVocabGame.setCellString(cellNumber, buttonValue);
                mSudokuGridView.setCellLabel(cellNumber, mVocabGame.getButtonString(buttonValue));


                if (mVocabGame.isCellCorrect(cellNumber)) {
                    //Correct number is placed in cell
                    mSudokuGridView.clearHighlightedCell();
                    mSudokuGridView.clearIncorrectCell();
                    mSoundPlayer.playPlaceCellSound();
                } else {
                    //Incorrect value has been placed in cell
                    mSudokuGridView.setIncorrectCell(cellNumber);
                    mSoundPlayer.playWrongSound();
                }

                //Redraw
                mSudokuGridView.invalidate();
            }
        }
    };

    public void onCheckAnswerPressed(View v) {
        //Check if cell is selected
        //If a cell is selected, check if that cell is correct
        int highlightedCell = mSudokuGridView.getHighlightedCell();
        if (highlightedCell != -1)
        {
            //Cell is right
            if (mVocabGame.isCellCorrect(highlightedCell)){
                mSudokuGridView.clearHighlightedCell();
                mSudokuGridView.invalidate();
                mSoundPlayer.playCorrectSound();
                Toast.makeText(getBaseContext(),
                        "The selected cell is correct!",
                        Toast.LENGTH_LONG).show();
            }

            //Cell is wrong
            else {
                mSudokuGridView.setIncorrectCell(highlightedCell);
                mSoundPlayer.playWrongSound();

            }
            mSudokuGridView.invalidate();
            return;
        }

        //Checks if the answers are right and displays the first wrong cell (if any)
        int potentialIndex = mVocabGame.checkGame();
        //Clear highlights / what cell is selected for input
        mSudokuGridView.clearHighlightedCell();

        //Case where answer is correct
        if (potentialIndex == -1) {
            mSoundPlayer.playCorrectSound();
            Toast.makeText(getBaseContext(),
                    "Congratulations, You've Won!",
                    Toast.LENGTH_LONG).show();
        }

        //Case where answer is incorrect
        else {
            mSudokuGridView.setIncorrectCell(potentialIndex);
            mSudokuGridView.setHighlightedCell(potentialIndex);
            mSoundPlayer.playWrongSound();
        }

        //Redraw grid
        mSudokuGridView.invalidate();
    }

    public void onClearCellPressed(View v) {
        int cellNumber = mSudokuGridView.getHighlightedCell();

        if (cellNumber == -1){
            //No cell is highlighted
            mSoundPlayer.playEmptyButtonSound();
        } else {
            mVocabGame.setCellString(cellNumber, 0);
            mSudokuGridView.setCellLabel(cellNumber, mVocabGame.getButtonString(0));
            mSudokuGridView.clearHighlightedCell();
            mSudokuGridView.clearIncorrectCell();
            mSoundPlayer.playClearCellSound();
            mSudokuGridView.invalidate();
        }
    }

    private void updateAllViewLabels() {
        for (int cellNumber = 0; cellNumber < 81; cellNumber++) {
            String label = mVocabGame.getCellString(cellNumber , mVocabGame.isLockedCell(cellNumber));

            if (mVocabGame.isLockedCell(cellNumber)) {
                label = SudokuGridView.LOCKED_FLAG + label;
            }
            mSudokuGridView.setCellLabel(cellNumber, label);
        }

        mSudokuGridView.invalidate();
    }

    /**
     * Bundles all information needed to save the game.
     * Info on what needs to be in the "save" bundle can be found in KeyConstants
     */
    private void makeSaveBundle(Bundle data) {
        data.putSerializable(DIFFICULTY_KEY, mVocabGame.getGameDifficulty());
        data.putSerializable(MODE_KEY, mVocabGame.getGameMode());
        data.putIntArray(CELL_VALUES_KEY, mVocabGame.getCellValues());
        data.putIntArray(SOLUTION_VALUES_KEY, mVocabGame.getSolutionValues());
        data.putBooleanArray(LOCKED_CELLS_KEY, mVocabGame.getLockedCells());
        data.putStringArray(NATIVE_WORDS_KEY, mVocabGame.getNativeWords());
        data.putStringArray(FOREIGN_WORDS_KEY, mVocabGame.getForeignWords());
    }
}
