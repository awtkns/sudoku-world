package com.sigma.sudokuworld.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
;
import com.sigma.sudokuworld.persistence.db.AppDatabase;
import com.sigma.sudokuworld.persistence.db.daos.GameSaveDao;
import com.sigma.sudokuworld.persistence.db.entities.GameSave;

import android.support.annotation.NonNull;

public class GameRepository {
    private GameSaveDao gameSaveDao;

    public GameRepository(@NonNull Application application) {
        gameSaveDao = AppDatabase.Companion.getInstance(application).getGameSaveDao();
    }

    public void newGame(GameSave game) {
        gameSaveDao.insert(game);
    }

    public void saveGame(GameSave gameSave) {
        gameSaveDao.update(gameSave);
    }

    public GameSave getGameSaveByID(int saveID) {
        return gameSaveDao.getGameSaveByID(saveID);
    }
}
