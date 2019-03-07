package com.sigma.sudokuworld.persistence;

import android.app.Application;
;
import com.sigma.sudokuworld.persistence.db.AppDatabase;
import com.sigma.sudokuworld.persistence.db.daos.GameSaveDao;
import com.sigma.sudokuworld.persistence.db.entities.Game;

import android.support.annotation.NonNull;

import java.util.List;

public class GameRepository {
    private GameSaveDao gameSaveDao;

    public GameRepository(@NonNull Application application) {
        gameSaveDao = AppDatabase.Companion.getInstance(application).getGameSaveDao();
    }

    public int newGame(Game game) {
        return (int) gameSaveDao.insert(game);
    }

    public void saveGame(Game game) {
        gameSaveDao.update(game);
    }

    public Game getGameSaveByID(int saveID) {
        return gameSaveDao.getGameSaveByID(saveID);
    }

    public List<Game> getAllGames() {
        return gameSaveDao.getAll();
    }
}
