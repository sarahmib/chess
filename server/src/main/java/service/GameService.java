package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;

public class GameService {
    private final GameDAO gameDataAccess;


    public GameService(GameDAO gameDataAccess) {
        this.gameDataAccess = gameDataAccess;
    }

    public void clearGames() throws DataAccessException {
        gameDataAccess.clearGames();
    }
}
