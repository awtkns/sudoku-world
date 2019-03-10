package com.sigma.sudokuworld.viewmodels;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.sigma.sudokuworld.game.GameDifficulty;
import com.sigma.sudokuworld.game.GameMode;
import com.sigma.sudokuworld.game.gen.PuzzleGenerator;
import com.sigma.sudokuworld.persistence.GameRepository;
import com.sigma.sudokuworld.persistence.WordSetRepository;
import com.sigma.sudokuworld.persistence.db.entities.Game;
import com.sigma.sudokuworld.persistence.db.entities.Set;
import com.sigma.sudokuworld.persistence.sharedpreferences.KeyConstants;
import com.sigma.sudokuworld.persistence.sharedpreferences.PersistenceService;

public class MenuViewModel extends SettingsViewModel {
    private GameRepository mGameRepository;
    private WordSetRepository mWordSetRepository;


    public MenuViewModel(@NonNull Application application) {
        super(application);

        mGameRepository = new GameRepository(application);
        mWordSetRepository = new WordSetRepository(application);
    }

    //Generates a new game based on stored settings
    public long generateNewGameWithStoredSettings() {
               Bundle puzzle = new PuzzleGenerator(3)
                .generatePuzzle(PersistenceService.loadDifficultySetting(mApplication));

        Game game = new Game(
                0,
                getSetID(),
                getGameDifficulty(),
                getGameMode(),
                puzzle.getIntArray(KeyConstants.CELL_VALUES_KEY),
                puzzle.getIntArray(KeyConstants.SOLUTION_VALUES_KEY),
                puzzle.getBooleanArray(KeyConstants.LOCKED_CELLS_KEY)
        );

        //Returns the saveID
        return mGameRepository.newGame(game);
    }

    /*
        Game Settings
     */
    public void setGameMode(GameMode gameMode) {
        PersistenceService.saveGameModeSetting(mApplication, gameMode);
    }

    public void setGameDifficulty(GameDifficulty difficulty) {
        PersistenceService.saveDifficultySetting(mApplication, difficulty);
    }

    public GameMode getGameMode() {
        return PersistenceService.loadGameModeSetting(mApplication);
    }

    public GameDifficulty getGameDifficulty() {
        return PersistenceService.loadDifficultySetting(mApplication);
    }

    public void setSet(Set set) {
        PersistenceService.saveSetSetting(mApplication, set.getSetID());
    }

    public Set getSet() {
        return mWordSetRepository.getSet(getSetID());
    }

    private long getSetID() {
        return PersistenceService.loadSetSettingSetting(mApplication);
    }
}
