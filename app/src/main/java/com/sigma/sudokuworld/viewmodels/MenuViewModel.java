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

public class MenuViewModel extends BaseSettingsViewModel {
    private GameRepository mGameRepository;
    private WordSetRepository mWordSetRepository;

    public GameDifficulty difficultySetting;
    public GameMode gameModeSetting;
    public Set setSetting;

    public MenuViewModel(@NonNull Application application) {
        super(application);

        mGameRepository = new GameRepository(application);
        mWordSetRepository = new WordSetRepository(application);

        difficultySetting = PersistenceService.loadDifficultySetting(mApplication);
        gameModeSetting = PersistenceService.loadGameModeSetting(mApplication);
        setSetting = mWordSetRepository.getSet(PersistenceService.loadSetSettingSetting(mApplication));
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        PersistenceService.saveDifficultySetting(mApplication, difficultySetting);
        PersistenceService.saveGameModeSetting(mApplication, gameModeSetting);
        PersistenceService.saveSetSetting(mApplication, setSetting.getSetID());
    }

    //Generates a new game based on stored settings
    public long generateNewGameWithStoredSettings() {
               Bundle puzzle = new PuzzleGenerator(3)
                .generatePuzzle(PersistenceService.loadDifficultySetting(mApplication));

        Game game = new Game(
                0,
                setSetting.getSetID(),
                difficultySetting,
                gameModeSetting,
                puzzle.getIntArray(KeyConstants.CELL_VALUES_KEY),
                puzzle.getIntArray(KeyConstants.SOLUTION_VALUES_KEY),
                puzzle.getBooleanArray(KeyConstants.LOCKED_CELLS_KEY)
        );

        //Returns the saveID
        return mGameRepository.newGame(game);
    }
}
