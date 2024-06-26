package service;

import chess.ChessGame;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
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

    public ChessGame getGame(Integer gameID) throws DataAccessException {
        GameData gameData = gameDataAccess.getGame(gameID);
        if (gameData == null) {
            return null;
        }
        return gameData.game();
    }

    public void clearGames() throws DataAccessException {
        gameDataAccess.clearGames();
    }

    public ListGamesResponse listGames(String authToken) throws DataAccessException {
        authService.isAuthorized(authToken);
        Collection<GameData> games = gameDataAccess.listGames();
        return new ListGamesResponse(games, null);
    }

    public CreateGameResponse createGame(CreateGameRequest request) throws DataAccessException {
        authService.isAuthorized(request.authToken());

        if (request.gameName() == null) {
            throw new BadRequestException("Error: bad request");
        }
        Integer gameID = gameDataAccess.createGame(request.gameName());
        return new CreateGameResponse(gameID, null);
    }

    public JoinGameResponse joinGame(JoinGameRequest request, String authToken) throws DataAccessException {
        AuthData authData = authService.isAuthorized(authToken);
        request = request.setUsername(authData.username());

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
            game.game().setWhiteUsername(request.username());
            gameDataAccess.updateGame(newGame);
        } else {
            GameData newGame = new GameData(game.gameID(), game.whiteUsername(), request.username(), game.gameName(), game.game());
            game.game().setBlackUsername(request.username());
            gameDataAccess.updateGame(newGame);
        }

        return new JoinGameResponse(null);
    }
}
