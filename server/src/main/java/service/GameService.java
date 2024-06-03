package service;

import chess.ChessGame;
import dataaccess.Exceptions.AlreadyTakenException;
import dataaccess.Exceptions.BadRequestException;
import dataaccess.Exceptions.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.JoinGameResponse;
import response.ListGamesResponse;

import java.util.Collection;

public class GameService {
    private final GameDAO gameDataAccess;
    private final AuthService authService;


    public GameService(GameDAO gameDataAccess, AuthService authService) {
        this.gameDataAccess = gameDataAccess;
        this.authService = authService;
    }

    public void clearGames() throws DataAccessException {
        gameDataAccess.clearGames();
    }

    public ListGamesResponse listGames() throws DataAccessException {
        Collection<GameData> games = gameDataAccess.listGames();
        return new ListGamesResponse(games, null);
    }

    public CreateGameResponse createGame(CreateGameRequest request) throws DataAccessException {
        if (request.gameName() == null) {
            throw new BadRequestException("Error: bad request");
        }
        Integer gameID = gameDataAccess.createGame(request.gameName());
        return new CreateGameResponse(gameID, null);
    }

    public JoinGameResponse joinGame(JoinGameRequest request) throws DataAccessException {
        if (request.gameID() == null || request.playerColor() == null) {
            throw new BadRequestException("Error: bad request");
        }
        GameData game = gameDataAccess.getGame(request.gameID());

        if (game.blackUsername() != null && request.playerColor() == ChessGame.TeamColor.BLACK) {
            throw new AlreadyTakenException("Error: already taken");
        }
        if (game.whiteUsername() != null && request.playerColor() == ChessGame.TeamColor.WHITE) {
            throw new AlreadyTakenException("Error: already taken");
        }

        if (request.playerColor() == ChessGame.TeamColor.WHITE) {
            GameData newGame = new GameData(game.gameID(), request.username(), game.blackUsername(), game.gameName(), game.game());
            gameDataAccess.updateGame(newGame);
        } else {
            GameData newGame = new GameData(game.gameID(), game.whiteUsername(), request.username(), game.gameName(), game.game());
            gameDataAccess.updateGame(newGame);
        }

        return new JoinGameResponse(null);
    }
}
