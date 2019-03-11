package com.sigma.sudokuworld.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
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

import java.util.List;

public class MenuViewModel extends BaseSettingsViewModel {
    private GameRepository mGameRepository;
    private WordSetRepository mWordSetRepository;
    private LiveData<List<Game>> mGames;

    public MenuViewModel(@NonNull Application application) {
        super(application);

        mGameRepository = new GameRepository(mApplication);
        mWordSetRepository = new WordSetRepository(mApplication);

        mGames = mGameRepository.getAllGames();
    }

    //Generates a new game based on stored settings
    public long generateNewGameWithStoredSettings() {
               Bundle puzzle = new PuzzleGenerator(3)
                .generatePuzzle(PersistenceService.loadDifficultySetting(mApplication));

        Game game = new Game(
                0,
                getSelectedSetID(),
                getSelectedGameDifficulty(),
                getSelectedGameMode(),
                puzzle.getIntArray(KeyConstants.CELL_VALUES_KEY),
                puzzle.getIntArray(KeyConstants.SOLUTION_VALUES_KEY),
                puzzle.getBooleanArray(KeyConstants.LOCKED_CELLS_KEY)
        );

        //Returns the saveID
        return mGameRepository.newGame(game);
    }

    public LiveData<List<Game>> getAllGameSaves() {
        return mGames;
    }

    public void deleteGame(Game game) {
        mGameRepository.deleteGame(game);
    }

    /*
        Game Settings
     */
    public void setSelectedGameMode(GameMode gameMode) {
        PersistenceService.saveGameModeSetting(mApplication, gameMode);
    }

    public void setSelectedGameDifficulty(GameDifficulty difficulty) {
        PersistenceService.saveDifficultySetting(mApplication, difficulty);
    }

    public GameMode getSelectedGameMode() {
        return PersistenceService.loadGameModeSetting(mApplication);
    }

    public GameDifficulty getSelectedGameDifficulty() {
        return PersistenceService.loadDifficultySetting(mApplication);
    }

    public void setSelectedSet(Set set) {
        PersistenceService.saveSetSetting(mApplication, set.getSetID());
    }

    public Set getSelectedSet() {
        return mWordSetRepository.getSet(getSelectedSetID());
    }

    public long getSelectedSetID() {
        return PersistenceService.loadSetSettingSetting(mApplication);
    }
}
