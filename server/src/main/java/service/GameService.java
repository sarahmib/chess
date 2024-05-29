package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import request.CreateGameRequest;
import response.CreateGameResponse;
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

    public CreateGameResponse createGame(CreateGameRequest request) throws DataAccessException {
        Integer gameID = gameDataAccess.createGame(request.gameName());
        return new CreateGameResponse(gameID, null);
    }
}
