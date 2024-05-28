package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import response.ListGamesResponse;

import java.util.Collection;

public class GameService {
    private final GameDAO gameDataAccess;


    public GameService(GameDAO gameDataAccess) {
        this.gameDataAccess = gameDataAccess;
    }

    public void clearGames() throws DataAccessException {
        gameDataAccess.clearGames();
    }

    public ListGamesResponse listGames() throws DataAccessException {
        Collection<GameData> games = gameDataAccess.listGames();
        return new ListGamesResponse(games, null);
    }
}
